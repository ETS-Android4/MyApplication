package com.example.william.my.module.sample.model

import androidx.lifecycle.*
import com.example.william.my.module.sample.repo.FlowRepository
import com.example.william.my.module.sample.utils.ThreadUtils
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel 应创建协程
 */
class FlowViewModel(private val dataSource: FlowRepository) : ViewModel() {

    private val _login = MutableLiveData<String>()

    val login: LiveData<String>
        get() = _login

    /**
     * -> flow
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            //打印线程
            ThreadUtils.isMainThread("CoroutinesViewModel getArticle")

            // 使用 collect 触发流并消耗其元素
            // Trigger the flow and consume its elements using collect
            dataSource.login(username, password)
                .onStart {
                    // 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的进度条
                }
                .catch { exception ->
                    // 捕获上游出现的异常
                    _login.postValue(exception.message.toString())
                }
                .onCompletion {
                    // 请求完成
                }
                .collect { article ->
                    // 更新视图
                    // Update View with the latest favorite news
                    _login.postValue(Gson().toJson(article))
                }
        }
    }

    /**
     * 使用 Flow 流构造方法 -> asLiveData()
     */
    fun loginByFLow(username: String, password: String): LiveData<String> {
        return dataSource.login(username, password)
            .map {
                Gson().toJson(it)
            }
            .asLiveData()//返回一个不可变的 LiveData
    }

    /**
     * 使用 Coroutine 协程构造方法 -> liveData<>
     */
    fun loginByCoroutine(username: String, password: String): LiveData<String> {
        return liveData {
            dataSource.login(username, password)
                .collect {
                    emit(Gson().toJson(it))
                }
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [DataBindingViewModel].
 */
object FlowVMFactory : ViewModelProvider.Factory {

    private val dataSource = FlowRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FlowViewModel(dataSource) as T
    }
}
