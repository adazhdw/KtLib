package com.adazhdw.ktlib.base

import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

// version 1.0
abstract class CoroutinesActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    protected val TAG = javaClass.simpleName + "------"
    protected val mHandler: Handler by lazy { Handler() }

    protected fun launchOnUI(
        error: ((Exception) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launch {
            tryCatch(block, {
                error?.invoke(it)
                "error:${it.message}".logE(TAG)
            }, {}, true)
        }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Exception) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Exception) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}