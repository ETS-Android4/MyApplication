package com.example.william.my.kotlin.model

import androidx.lifecycle.*
import com.example.william.my.kotlin.bean.LoginData
import com.example.william.my.kotlin.repository.ExampleRepository
import com.example.william.my.kotlin.repository.LoginRepository
import com.example.william.my.kotlin.result.NetworkResult
import com.example.william.my.kotlin.utils.ThreadUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class CoroutinesViewModel : ViewModel() {

    private val _login = MutableLiveData<String>()

    val login: LiveData<String>
        get() = _login

    fun login(username: String, password: String) {

        // 创建一个新的协程，然后在 I/O 线程上执行网络请求
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.IO) {

            //打印线程
            ThreadUtils.isMainThread("CoroutinesViewModel login")

            val bodyJson = "username=$username&password=$password"

            // 执行网络请求 并 挂起，直至请求完成
            // Make the network call and suspend execution until it finishes
            val result = try {
                LoginRepository().login(bodyJson)
            } catch (e: Exception) {
                NetworkResult.NetworkError(Exception("Network request failed"))
            }

            // 向用户展示网络请求结果
            // Display result of the network request to the user
            when (result) {
                is NetworkResult.Loading -> {
                    _login.postValue("加载中……")
                }
                is NetworkResult.OK<LoginData> -> {
                    _login.postValue(Gson().toJson(result.data))
                }
                is NetworkResult.NetworkError -> {
                    _login.postValue(result.exception.message)
                }
            }
        }
    }

    private val _articles = MutableLiveData<String>()

    val articles: LiveData<String>
        get() = _articles

    fun getArticles() {
        viewModelScope.launch {
            //打印线程
            ThreadUtils.isMainThread("CoroutinesViewModel getArticles")

            // 使用 collect 触发流并消耗其元素
            // Trigger the flow and consume its elements using collect
            ExampleRepository().getArticles
                .onStart {
                    // 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的进度条
                }
                .catch { exception ->
                    // 捕获上游出现的异常
                    _articles.postValue(exception.message.toString())
                }
                .onCompletion {
                    // 请求完成
                }
                .collect { article ->
                    // 更新视图
                    // Update View with the latest favorite news
                    _articles.postValue(Gson().toJson(article))
                }
        }
    }

    /**
     * 使用 LiveData 协程构造方法
     */
    fun getArticles2() = liveData<String> {
        //打印线程
        ThreadUtils.isMainThread("CoroutinesViewModel getArticles2")

        ExampleRepository().getArticles
            .collectLatest { article ->
                // 在一段时间内发送多次数据，只会接受最新的一次发射过来的数据
                emit(Gson().toJson(article))
            }
    }

    fun getArticles3() =
        ExampleRepository().getArticles
            .map {
                Gson().toJson(it)
            }
            .asLiveData()//返回一个不可变的 LiveData
}