package com.example.william.my.module.sample.state

import com.example.william.my.bean.data.ArticleDetailBean

sealed class ArticleUiState {
    object Idle : ArticleUiState()
    object Loading : ArticleUiState()
    data class Users(val list: List<ArticleDetailBean>) : ArticleUiState()
    data class Error(val error: String?) : ArticleUiState()
}