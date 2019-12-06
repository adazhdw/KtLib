package com.adazhdw.ktlib.base

import androidx.fragment.app.Fragment
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutinesFragment : Fragment(), CoroutineScope {

    protected val TAG = this.javaClass.simpleName + "------"
    private val myJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + myJob

    override fun onDestroyView() {
        super.onDestroyView()
        myJob.cancel()
    }

    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        launch { tryCatch(block, { "error:${it.message}".logE(TAG) }, {}, true) }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
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

}