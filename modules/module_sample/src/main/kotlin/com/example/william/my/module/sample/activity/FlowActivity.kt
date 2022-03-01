package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.model.FlowVMFactory
import com.example.william.my.module.sample.model.FlowViewModel

/**
 * 数据流
 * https://developer.android.google.cn/kotlin/flow
 */
@Route(path = ARouterPath.Sample.Sample_FLow)
class FlowActivity : BaseActivity() {

    private val mViewModel: FlowViewModel by viewModels {
        FlowVMFactory
    }

    private val username = "17778060027"
    private val password = "ww123456"

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        observeViewModel()
    }

    private fun observeViewModel() {
        login()
    }

    private fun login() {
        mViewModel.login.observe(this) {
            mBinding.textView.text = it
        }
        mViewModel.login(username, password)
    }

    /**
     * 通过 使用 Flow 流构造方法 -> asLiveData() 直接返回 LiveData
     */
    private fun loginByFlow() {
        mViewModel.loginByFLow(username, password).observe(this) {
            mBinding.textView.text = it
        }
    }

    /**
     * 通过 使用 Coroutine 协程构造方法 -> liveData<> 直接返回 LiveData
     */
    private fun loginByCoroutine() {
        mViewModel.loginByCoroutine(username, password).observe(this) {
            mBinding.textView.text = it
        }
    }
}