package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.model.LiveDataVMFactory
import com.example.william.my.module.sample.model.LiveDataViewModel

/**
 * LiveData
 * https://developer.android.google.cn/topic/libraries/architecture/livedata
 * <p>
 * LiveData对象不应存在于存储库中。
 * LiveData objects should not live in the repository.
 */
@Route(path = ARouterPath.Sample.Sample_LiveData)
class LiveDataActivity : BaseActivity() {

    private val mViewModel: LiveDataViewModel by viewModels {
        LiveDataVMFactory
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