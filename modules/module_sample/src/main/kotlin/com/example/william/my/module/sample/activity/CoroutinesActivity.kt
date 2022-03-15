package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.model.CoroutinesVMFactory
import com.example.william.my.module.sample.model.CoroutinesViewModel

/**
 * Android 上的 Kotlin 协程
 * https://developer.android.google.cn/kotlin/coroutines
 */
@Route(path = ARouterPath.Sample.Sample_Coroutines)
class CoroutinesActivity : AppCompatActivity() {

    private val mViewModel: CoroutinesViewModel by viewModels {
        CoroutinesVMFactory
    }

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        observeViewModel()
    }

    private fun observeViewModel() {
        mViewModel.login.observe(this) {
            mBinding.textView.text = it
        }
        mViewModel.login("17778060027", "ww123456")
    }
}