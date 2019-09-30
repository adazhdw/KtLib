package com.adazhdw.baselibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment : CoroutinesFragment() {

    /**
     * 返回布局Id
     */
    protected abstract val layoutId: Int

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化View
     */
    protected abstract fun initView(view: View)

    /**
     * 是否需要EventBus
     */
    protected open fun needEventBus(): Boolean = false

    /**
     * 网络请求开始
     */
    abstract fun requestStart()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initData()
        requestStart()
    }

    override fun onStart() {
        super.onStart()
        if (needEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (needEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

}