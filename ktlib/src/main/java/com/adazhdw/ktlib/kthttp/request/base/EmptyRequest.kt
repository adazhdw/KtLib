package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.KtHttp
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.util.RequestUrlUtil
import okhttp3.RequestBody
import okhttp3.internal.EMPTY_REQUEST

/**
 * author：daguozhu
 * date-time：2020/9/3 16:25
 * description：
 **/
abstract class EmptyRequest(
    url: String,
    params: Params
) : BaseRequest(url, params) {
    protected val mUrl: String

    init {
        val commonParams = mutableMapOf<String, String>().apply {
            putAll(KtHttp.ktHttp.getCommonParams())
            putAll(params.params)
        }
        mUrl = RequestUrlUtil.getFullUrl2(url, commonParams, params.urlEncoder)
    }

    final override fun getRequestBody(): RequestBody = EMPTY_REQUEST

}