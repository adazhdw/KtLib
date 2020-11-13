package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.base.EmptyRequest
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:39
 * description：
 **/
class HeadRequest(
    url: String,
    params: Params
) : EmptyRequest(url, params) {

    override fun getRequest(requestBody: RequestBody): Request {
        return requestBuilder().url(mUrl).head().tag(params.tag).build()
    }


}