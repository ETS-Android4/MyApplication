package com.example.william.my.kotlin.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.william.my.kotlin.repository.ArticlesRepository
import com.example.william.my.module.bean.ArticlesBean
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ArticlesViewModel(private val articlesRepository: ArticlesRepository) : ViewModel() {

    private val _articles = MutableLiveData<ArticlesBean>()

    val articles: LiveData<ArticlesBean>
        get() = _articles

    fun getArticle() {
        viewModelScope.launch {
            // 使用 collect 触发流并消耗其元素
            // Trigger the flow and consume its elements using collect
            articlesRepository.getArticles
                // Intermediate catch operator. If an exception is thrown,
                // catch and update the UI
                .catch { exception ->
                    Log.e("TAG", exception.message.toString())
                }
                // If an error happens, emit the last cached values
                .onEach {

                }
                // 更新视图
                // Update View with the latest favorite news
                .collect { article ->
                    _articles.postValue(article)
                }
        }
    }
}