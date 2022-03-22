package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.result.NetworkResult
import com.example.william.my.module.sample.viewmodel.StateFlowVMFactory
import com.example.william.my.module.sample.viewmodel.StateFlowViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * Android 上的 Kotlin 数据流
 * https://developer.android.google.cn/kotlin/flow
 */
@Route(path = ARouterPath.Sample.Sample_StateFlow)
class StateFlowActivity : BaseActivity() {

    private val mViewModel: StateFlowViewModel by viewModels {
        StateFlowVMFactory
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
        // 在生命周期范围内启动协同程序
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // 每次生命周期处于已启动状态（或更高）时，在新的协同路由中启动该块，并在其 STOPPED 时取消该块。
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 触发流并开始监听值。
                // Note that this happens when lifecycle is STARTED and stops collecting when the lifecycle is STOPPED
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops collecting when the lifecycle is STOPPED
                mViewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is NetworkResult.Loading -> showLoading()
                        is NetworkResult.Success -> showFavoriteNews(uiState.data)
                        is NetworkResult.Error -> showError(uiState.exception)
                    }
                }
            }
        }
        mViewModel.login(username, password)
    }

    private fun showLoading() {

    }

    private fun showFavoriteNews(news: Any) {
        mBinding.textView.text = Gson().toJson(news)
    }

    private fun showError(exception: Throwable) {
        mBinding.textView.text = exception.message
    }
}