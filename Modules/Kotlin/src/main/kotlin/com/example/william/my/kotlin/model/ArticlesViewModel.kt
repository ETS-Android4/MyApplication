package com.example.william.my.kotlin.model

import androidx.lifecycle.*
import com.example.william.my.kotlin.repository.ArticlesRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ArticlesViewModel(private val articlesRepository: ArticlesRepository) : ViewModel() {

    private val _articles = MutableLiveData<String>()

    val articles: LiveData<String>
        get() = _articles

    fun getArticles() {
        // 使用collect触发流并消耗其元素
        // Trigger the flow and consume its elements using collect
        viewModelScope.launch {
            // 使用 collect 触发流并消耗其元素
            // Trigger the flow and consume its elements using collect
            articlesRepository.getArticles
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
        articlesRepository.getArticles
            // 在一段时间内发送多次数据，只会接受最新的一次发射过来的数据
            .collectLatest { article ->
                emit(Gson().toJson(article))
            }
    }
}