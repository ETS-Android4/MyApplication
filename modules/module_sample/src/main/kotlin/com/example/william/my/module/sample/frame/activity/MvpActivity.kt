package com.example.william.my.module.sample.frame.activity

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseFragmentActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.frame.fragment.MvpFragment

/**
 * MVP：Model-View-Presenter
 */
@Route(path = ARouterPath.Sample.Sample_MVP)
class MvpActivity : BaseFragmentActivity() {

    override fun setFragment(): Fragment {
        return MvpFragment()
    }
}