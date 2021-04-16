package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDetailBean

interface KtArticleDataSource {
    val article: LiveData<ArticleBean>
    suspend fun fetchNewData()
    suspend fun loadMoreData()

    val articleList: LiveData<List<ArticleDetailBean>>
}