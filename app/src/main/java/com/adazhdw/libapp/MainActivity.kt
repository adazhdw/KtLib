package com.adazhdw.libapp

import com.adazhdw.ktlib.base.activity.ViewBindingActivity
import com.adazhdw.ktlib.core.viewbinding.inflate
import com.adazhdw.ktlib.core.viewbinding.inflate3
import com.adazhdw.ktlib.ext.addFragment
import com.adazhdw.libapp.databinding.ActivityMainBinding

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    override val viewBinding by inflate3<ActivityMainBinding>()

    override fun initView() {
        viewBinding.container
        addFragment(WxChaptersFragment(), R.id.container)
    }

}
