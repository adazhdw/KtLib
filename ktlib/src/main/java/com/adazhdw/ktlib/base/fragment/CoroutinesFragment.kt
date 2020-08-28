package com.adazhdw.ktlib.base.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class CoroutinesFragment : Fragment() {

    protected val TAG = this.javaClass.name + "------"

    protected fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenResumed(block)
    }

    protected fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenCreated(block)
    }

    protected fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenStarted(block)
    }

    protected fun launch(
        error: ((Exception) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) = launchOnUI(error, block)

    protected fun launchOnUI(
        error: ((Exception) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch {
            tryCatch(
                tryBlock = block,
                catchBlock = {
                    error?.invoke(it)
                    "error:${it.message}".logE(TAG)
                },
                finallyBlock = {},
                handleCancellationExceptionManually = true
            )
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

}