package com.example.william.my.module.sample.frame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.william.my.module.sample.frame.data.source.ArticleDataSource
import com.example.william.my.module.sample.frame.intent.ArticleIntent
import com.example.william.my.module.sample.frame.intent.ArticleViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ArticleStateFlowViewModel(private val dataSource: ArticleDataSource) : ViewModel() {

    val intent = Channel<ArticleIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ArticleViewState>(ArticleViewState.Loading)
    val state: StateFlow<ArticleViewState>
        get() = _state

    init {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is ArticleIntent.LoadArticleIntent -> loadArticle(it.page)
                }
            }
        }
        loadArticle(0)
    }

    private fun loadArticle(page: Int) {
        viewModelScope.launch {
            _state.value = ArticleViewState.Loading
            _state.value =
                try {
                    ArticleViewState.Success(dataSource.getArticleSuspend(page).data.datas)
                } catch (e: Exception) {
                    ArticleViewState.Error(e.message)
                }
        }
    }
}