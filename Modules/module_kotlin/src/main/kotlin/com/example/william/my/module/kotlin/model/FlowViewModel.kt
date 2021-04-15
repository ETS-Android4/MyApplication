package com.example.william.my.module.kotlin.model

import androidx.lifecycle.*
import com.example.william.my.module.kotlin.data.LoginData
import com.example.william.my.module.kotlin.repo.ArticleRepository
import com.example.william.my.module.kotlin.repo.LoginRepository
import com.example.william.my.module.kotlin.result.NetworkResult
import com.example.william.my.module.kotlin.utils.ThreadUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ViewModel 应创建协程
 */
class FlowViewModel : ViewModel() {

    private val _login = MutableLiveData<String>()

    val login: LiveData<String>
        get() = _login

    /**
     * HttpURLConnection
     */
    fun login(username: String, password: String) {

        // 在UI线程上创建一个新的协同程序
        // Create a new coroutine on the UI thread
        viewModelScope.launch(Dispatchers.IO) {

            //打印线程
            ThreadUtils.isMainThread("CoroutinesViewModel login")

            val bodyJson = "username=$username&password=$password"

            // 执行网络请求 并 挂起，直至请求完成
            // Make the network call and suspend execution until it finishes
            val result = try {
                LoginRepository().login(bodyJson)
            } catch (e: Exception) {
                NetworkResult.Error(Exception("Network request failed"))
            }

            // 向用户展示网络请求结果
            // Display result of the network request to the user
            when (result) {
                is NetworkResult.Loading -> {
                    _login.postValue("加载中……")
                }
                is NetworkResult.Success<LoginData> -> {
                    _login.postValue(Gson().toJson(result.data))
                }
                is NetworkResult.Error -> {
                    _login.postValue(result.exception.message)
                }
            }
        }
    }

    private val _article = MutableLiveData<String>()

    val article: LiveData<String>
        get() = _article

    fun getArticle() {
        viewModelScope.launch {
            //打印线程
            ThreadUtils.isMainThread("CoroutinesViewModel getArticle")

            // 使用 collect 触发流并消耗其元素
            // Trigger the flow and consume its elements using collect
            ArticleRepository().getArticle
                .onStart {
                    // 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的进度条
                }
                .catch { exception ->
                    // 捕获上游出现的异常
                    _article.postValue(exception.message.toString())
                }
                .onCompletion {
                    // 请求完成
                }
                .collect { article ->
                    // 更新视图
                    // Update View with the latest favorite news
                    _article.postValue(Gson().toJson(article))
                }
        }
    }

    /**
     * 使用 Flow 流构造方法 -> asLiveData()
     */
    fun getArticleByFlow() =
        ArticleRepository().getArticle
            .map {
                Gson().toJson(it)
            }
            .asLiveData()//返回一个不可变的 LiveData

    /**
     * 使用 Coroutine 协程构造方法 -> liveData<>
     */
    fun getArticleByCoroutine() = liveData<String> {
        ArticleRepository().getArticle
            .collect {
                emit(Gson().toJson(it))
            }
    }
}