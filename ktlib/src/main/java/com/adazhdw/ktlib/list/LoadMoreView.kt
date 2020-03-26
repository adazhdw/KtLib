package com.adazhdw.ktlib.list


@Deprecated("")
interface LoadMoreView {

    /**
     * Show progress.
     */
    fun loading()

    /**
     * Load finish, handle result.
     */
    fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean)

    /**
     * Load error.
     */
    fun onLoadError(errorCode: Int, errorMsg: String?)
}
