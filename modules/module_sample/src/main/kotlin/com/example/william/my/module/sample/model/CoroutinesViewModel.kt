package com.example.william.my.module.sample.model

import androidx.lifecycle.*
import com.example.william.my.bean.data.LoginBean
import com.example.william.my.module.sample.repo.CoroutinesRepository
import com.example.william.my.module.sample.result.NetworkResult
import com.example.william.my.module.sample.utils.ThreadUtils
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * Android 上的 Kotlin 协程
 * https://developer.android.google.cn/kotlin/coroutines
 */
class CoroutinesViewModel(private val dataSource: CoroutinesRepository) : ViewModel() {

    private val _login = MutableLiveData<String>()

    val login: LiveData<String>
        get() = _login

    fun login(username: String, password: String) {

        // 在UI线程上创建一个新的协同程序
        // Create a new coroutine on the UI thread
        viewModelScope.launch {
            //打印线程
            ThreadUtils.isMainThread("CoroutinesViewModel login")

            // 执行网络请求 并 挂起，直至请求完成
            // Make the network call and suspend execution until it finishes
            val result: NetworkResult<LoginBean> =
                try {
                    dataSource.loginByHttpURL(username, password)
                } catch (e: Exception) {
                    NetworkResult.Error(Exception("Network request failed"))
                }

            // 向用户展示网络请求结果
            // Display result of the network request to the user
            when (result) {
                is NetworkResult.Loading -> {
                    _login.postValue("加载中……")
                }
                is NetworkResult.Success<LoginBean> -> {
                    _login.postValue(Gson().toJson(result.data))
                }
                is NetworkResult.Error -> {
                    _login.postValue(result.exception.message)
                }
            }
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [CoroutinesViewModel].
 */
object CoroutinesVMFactory : ViewModelProvider.Factory {

    private val dataSource = CoroutinesRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CoroutinesViewModel(dataSource) as T
    }
}