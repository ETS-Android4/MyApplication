package com.example.william.my.module.sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.bean.data.LoginBean
import com.example.william.my.module.sample.repo.FlowRepository
import com.example.william.my.module.sample.result.NetworkResult
import com.example.william.my.module.sample.utils.ThreadUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * StateFlow
 */
class StateFlowViewModel(private val dataSource: FlowRepository) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<NetworkResult<LoginBean>>(NetworkResult.Loading)

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<NetworkResult<LoginBean>> = _uiState

    fun login(username: String, password: String) {
        // 在UI线程上创建一个新的协同程序
        // Create a new coroutine on the UI thread
        viewModelScope.launch {
            //打印线程
            ThreadUtils.isMainThread("FlowViewModel getArticle")

            val result: Flow<LoginBean> = dataSource.loginByFlow(username, password)

            // 使用 collect 触发流并消耗其元素
            // Trigger the flow and consume its elements using collect
            result
                .onStart {
                    // 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的进度条
                    _uiState.value = NetworkResult.Loading
                }
                .catch { exception ->
                    // 捕获上游出现的异常
                    _uiState.value = NetworkResult.Error(exception)
                }
                .onCompletion {
                    // 请求完成
                }
                .collect { article ->
                    // 更新视图
                    // Update View with the latest favorite news
                    _uiState.value = NetworkResult.Success(article)
                }
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [LiveDataViewModel].
 */
object StateFlowVMFactory : ViewModelProvider.Factory {

    private val dataSource = FlowRepository(Dispatchers.IO)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return StateFlowViewModel(dataSource) as T
    }
}
