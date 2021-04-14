package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.module.bean.ArticleBean

interface KtArticleDataSource {
    val article: LiveData<ArticleBean>
    suspend fun fetchNewData()
    suspend fun loadMoreData()
}