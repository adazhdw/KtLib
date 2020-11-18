package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.exception.ExceptionHelper
import com.adazhdw.ktlib.kthttp.exception.NetException
import com.adazhdw.ktlib.kthttp.request.RequestCallProxy
import com.adazhdw.ktlib.kthttp.util.LifecycleUtil
import okhttp3.Call
import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/11/18 9:30
 * description：
 **/

open class OkHttpCallbackImpl(
    callProxy: RequestCallProxy,
    private val requestCallback: RequestCallback?
) : OkHttpCallback(callProxy, requestCallback?.mLifecycleOwner) {
    init {
        KtExecutors.mainThread.execute {
            if (isLifecycleActive()) {
                requestCallback?.onStart(mCallProxy.call)
            }
        }
    }

    override fun onResponse(response: Response) {
        val result = ExceptionHelper.getNotNullResult(response).string()
        KtExecutors.mainThread.execute {
            if (isLifecycleActive()) {
                requestCallback?.onHttpResponse(response, result)
                requestCallback?.onFinish()
            }
        }
    }

    override fun onFailure(e: Exception, call: Call) {
        e.printStackTrace()
        val ex: NetException = ExceptionHelper.callError(e)
        KtExecutors.mainThread.execute {
            if (isLifecycleActive()) {
                requestCallback?.onFailure(ex, ex.code, ex.msg)
                requestCallback?.onFinish()
            }
        }
    }

    /**
     * 判断当前宿主是否处于活动状态
     */
    private fun isLifecycleActive() = LifecycleUtil.isLifecycleActive(mLifecycleOwner)

}