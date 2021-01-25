package com.example.william.my.kotlin.model

import androidx.lifecycle.*
import com.example.william.my.kotlin.bean.LoginData
import com.example.william.my.kotlin.repository.ExampleRepository
import com.example.william.my.kotlin.source.ExampleDataSource
import com.example.william.my.kotlin.utils.NetworkResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ExampleViewModel : ViewModel() {

    private val _login = MutableLiveData<String>()

    val login: LiveData<String>
        get() = _login

    fun login(username: String, password: String) {

        // 创建一个新的协程，然后在 I/O 线程上执行网络请求
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.IO) {

            val bodyJson = "username=$username&password=$password"

            // 执行网络请求 并 挂起，直至请求完成
            // Make the network call and suspend execution until it finishes
            val result = try {
                ExampleDataSource().login(bodyJson)
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
        // 使用collect触发流并消耗其元素
        // Trigger the flow and consume its elements using collect
        viewModelScope.launch {
            // 使用 collect 触发流并消耗其元素
            // Trigger the flow and consume its elements using collect
            ExampleRepository().getArticles
                // 如果抛出异常，捕获并更新UI
                // Intermediate catch operator. If an exception is thrown,
                // catch and update the UI
                .catch { exception ->
                    _articles.postValue(exception.message.toString())
                }
                // If an error happens, emit the last cached values
                .onEach {
                    //Log.e("TAG", Gson().toJson(it))
                }
                // 更新视图
                // Update View with the latest favorite news
                .collect { article ->
                    _articles.postValue(Gson().toJson(article))
                }

        }
    }

    /**
     * 使用 LiveData 协程构造方法
     */
    fun getArticles2() = liveData<String> {
        ExampleRepository().getArticles
            // 在一段时间内发送多次数据，只会接受最新的一次发射过来的数据
            .collectLatest { article ->
                emit(Gson().toJson(article))
            }
    }
}