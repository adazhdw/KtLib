package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.BaseRequestCallback
import com.adazhdw.ktlib.kthttp.constant.*
import com.adazhdw.ktlib.kthttp.httpbuilder.KtHttpBuilder
import com.adazhdw.ktlib.kthttp.param.KParams
import okhttp3.Call
import okhttp3.OkHttpClient

object KtHttp {

    /**
     * Get请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun get(
        url: String,
        params: KParams? = null,
        callback: BaseRequestCallback? = null,
        timeout: Long = HttpConstant.DEFAULT_TIMEOUT
    ) {
        val builder: OkHttpClient.Builder = KtHttpBuilder.obtainBuilder(timeout)
        executeRequest(GET, url, params ?: KParams(), builder, callback)
    }

    /**
     * Post请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun post(
        url: String,
        params: KParams? = null,
        callback: BaseRequestCallback? = null,
        timeout: Long = HttpConstant.DEFAULT_TIMEOUT
    ) {
        val builder: OkHttpClient.Builder = KtHttpBuilder.obtainBuilder(timeout)
        executeRequest(POST, url, params ?: KParams(), builder, callback)
    }

    /**
     * put请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun put(
        url: String,
        params: KParams? = null,
        callback: BaseRequestCallback? = null,
        timeout: Long = HttpConstant.DEFAULT_TIMEOUT
    ) {
        val builder: OkHttpClient.Builder = KtHttpBuilder.obtainBuilder(timeout)
        executeRequest(PUT, url, params ?: KParams(), builder, callback)
    }

    /**
     * delete请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun delete(
        url: String,
        params: KParams? = null,
        callback: BaseRequestCallback? = null,
        timeout: Long = HttpConstant.DEFAULT_TIMEOUT
    ) {
        val builder: OkHttpClient.Builder = KtHttpBuilder.obtainBuilder(timeout)
        executeRequest(DELETE, url, params ?: KParams(), builder, callback)
    }

    /**
     * head请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun head(
        url: String,
        params: KParams? = null,
        callback: BaseRequestCallback? = null,
        timeout: Long = HttpConstant.DEFAULT_TIMEOUT
    ) {
        val builder: OkHttpClient.Builder = KtHttpBuilder.obtainBuilder(timeout)
        executeRequest(HEAD, url, params ?: KParams(), builder, callback)
    }

    /**
     * patch请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun patch(
        url: String,
        params: KParams? = null,
        callback: BaseRequestCallback? = null,
        timeout: Long = HttpConstant.DEFAULT_TIMEOUT
    ) {
        val builder: OkHttpClient.Builder = KtHttpBuilder.obtainBuilder(timeout)
        executeRequest(PATCH, url, params ?: KParams(), builder, callback)
    }

    private fun executeRequest(
        method: Method,
        url: String,
        params: KParams,
        builder: OkHttpClient.Builder,
        callback: BaseRequestCallback? = null
    ): Call {
        if (params.tag.isEmpty()) params.tag = url
        val request = KtHttpRequest(
            method = method,
            url = url,
            params = params,
            builder = builder,
            callback = callback
        )
        return request.execute()
    }

    fun cancel(url: String) {
        if (url.isNotBlank()) {
            val call: Call? = KtHttpCallManager.instance.getCall(url)
            call?.cancel()
            KtHttpCallManager.instance.removeCall(url)
        }
    }
}