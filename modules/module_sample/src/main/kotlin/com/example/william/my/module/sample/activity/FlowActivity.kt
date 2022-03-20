package com.example.william.my.module.sample.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding

/**
 * Android 上的 Kotlin 数据流
 * https://developer.android.google.cn/kotlin/flow
 */
@Route(path = ARouterPath.Sample.Sample_FLow)
class FlowActivity : BaseActivity() {

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        observeViewModel()
    }

    private fun observeViewModel() {

    }
}