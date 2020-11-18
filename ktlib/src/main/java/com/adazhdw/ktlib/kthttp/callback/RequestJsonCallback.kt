package com.adazhdw.ktlib.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.kthttp.KtConfig
import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.kthttp.util.ClazzUtil
import com.google.gson.JsonParseException
import okhttp3.Response
import java.lang.reflect.Type

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: Gson回调转换泛型类 T
 */
abstract class RequestJsonCallback<T : Any>(owner: LifecycleOwner?) : RequestCallbackImpl(owner) {
    private val mType: Type?

    init {
        mType = getSuperclassTypeParameter(javaClass)
    }

    override fun onHttpResponse(httpResponse: Response, result: String) {
        super.onHttpResponse(httpResponse, result)
        try {
            val data = KtConfig.converter.convert<T>(result, mType, KtConfig.needDecodeResult)
            this.onSuccess(data)
        } catch (e: JsonParseException) {
            onFailure(e, HttpConstant.ERROR_JSON_PARSE_EXCEPTION, "Data parse error${e.message}")
        }
    }

    abstract fun onSuccess(data: T)
    abstract fun onError(code: Int, msg: String?)

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        super.onFailure(e, code, msg)
        this.onError(code, msg)
    }

    private fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
        return ClazzUtil.getClassType(subclass)
    }

}