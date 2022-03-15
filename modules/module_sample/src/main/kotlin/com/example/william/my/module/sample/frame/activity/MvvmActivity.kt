package com.example.william.my.module.sample.frame.activity

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseFragmentActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.frame.fragment.MvvmFragment

/**
 * MVVM：Model-View-ViewModel
 */
@Route(path = ARouterPath.Sample.Sample_MVVM)
class MvvmActivity : BaseFragmentActivity() {

    override fun setFragment(): Fragment {
        return MvvmFragment()
    }
}