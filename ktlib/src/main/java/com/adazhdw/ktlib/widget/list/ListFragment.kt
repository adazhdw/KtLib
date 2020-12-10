package com.adazhdw.ktlib.widget.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.ktlib.R
import com.adazhdw.ktlib.base.fragment.ViewBindingFragment
import com.adazhdw.ktlib.databinding.FragmentListLayoutBinding
import com.adazhdw.ktlib.ext.dp2px
import com.adazhdw.ktlib.widget.LinearSpacingItemDecoration

/**
 * daguozhu
 * create at 2020/4/13 10:11
 * description:
 */
abstract class ListFragment<T : Any, A : LoadMoreAdapter<T>> : ViewBindingFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_list_layout

    private lateinit var viewBinding: FragmentListLayoutBinding
    protected val mDataAdapter by lazy { getDataAdapter() }
    private var currPage = 0
    open val loadMoreEnabled: Boolean = true
    protected val mData: List<T>
        get() = mDataAdapter.data

    final override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes resId: Int
    ): ViewDataBinding {
        viewBinding = FragmentListLayoutBinding.inflate(inflater, container, false)
        return viewBinding
    }

    override fun initView(view: View) {
        viewBinding.swipe.setOnRefreshListener {
            requestStart()
        }
        viewBinding.dataRV.setLoadMoreEnabled(loadMoreEnabled)
        viewBinding.dataRV.layoutManager = getLayoutManager()
        viewBinding.dataRV.addItemDecoration(itemDecoration())
        viewBinding.dataRV.adapter = mDataAdapter
        viewBinding.dataRV.setLoadMoreListener(object : LoadMoreRV.LoadMoreListener {
            override fun loadMore() {
                requestData(false)
            }
        })
        rvExtra(viewBinding.dataRV)
    }

    override fun requestStart() {
        refresh()
    }

    fun refresh() {
        if (!viewBinding.swipe.isRefreshing) {
            viewBinding.swipe.isRefreshing = true
        }
        requestData(viewBinding.swipe.isRefreshing)
    }

    private fun requestData(refreshing: Boolean) {
        if (refreshing) {
            currPage = startAtPage()
            viewBinding.dataRV.setLoadMoreEnabled(false)
            viewBinding.swipe.isEnabled = true
        } else {
            viewBinding.swipe.isEnabled = false
            viewBinding.dataRV.setLoadMoreEnabled(true)
        }
        onLoad(currPage, object : LoadDataCallback<T> {
            override fun onSuccess(data: List<T>, hasMore: Boolean) {
                if (data.isNotEmpty()) currPage += 1
                if (refreshing) {
                    mDataAdapter.replaceData(data)
                    if (data.isNotEmpty()) {
                        viewBinding.dataRV.scrollToPosition(0)
                    }
                } else {
                    mDataAdapter.addData(data)
                }
                viewBinding.swipe.isEnabled = true
                viewBinding.swipe.isRefreshing = false
                viewBinding.dataRV.setLoadMoreEnabled(true)
                viewBinding.dataRV.setIsLoading(false)
                if (hasMore) {
                    mDataAdapter.loading()
                } else {
                    mDataAdapter.loadAll()
                }
            }

            override fun onFail(code: Int, msg: String?) {
                if (refreshing) viewBinding.swipe.isRefreshing = false
                viewBinding.swipe.isEnabled = true
                viewBinding.swipe.isRefreshing = false
                viewBinding.dataRV.setLoadMoreEnabled(true)
                viewBinding.dataRV.setIsLoading(false)
                mDataAdapter.loadError()
                onError(code, msg)
            }
        })
    }

    abstract fun onLoad(page: Int, callback: LoadDataCallback<T>)
    abstract fun getDataAdapter(): A
    open fun startAtPage() = 0
    open fun perPage() = 20
    open fun onError(code: Int, msg: String?) {}
    open fun rvExtra(recyclerView: RecyclerView) {}
    open fun getLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    open fun itemDecoration(): RecyclerView.ItemDecoration =
        LinearSpacingItemDecoration(dp2px(15f), LinearLayoutManager.VERTICAL, true)


    interface LoadDataCallback<T> {
        fun onSuccess(data: List<T>, hasMore: Boolean)
        fun onFail(code: Int, msg: String?)
    }

}