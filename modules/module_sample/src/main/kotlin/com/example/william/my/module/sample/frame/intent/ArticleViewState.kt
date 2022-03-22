package com.example.william.my.module.sample.frame.intent

import com.example.william.my.bean.data.ArticleDetailBean

sealed class ArticleIntent {
    class LoadArticleIntent(val page: Int) : ArticleIntent()
}

sealed class ArticleViewState {
    object Loading : ArticleViewState()
    data class Success(val news: List<ArticleDetailBean>) : ArticleViewState()
    data class Error(val error: String?) : ArticleViewState()
}
