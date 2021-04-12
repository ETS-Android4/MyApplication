package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.module.bean.ArticleBean
import kotlinx.coroutines.flow.Flow

interface KtArticleDataSource {
    fun getArticleBean(): Flow<ArticleBean>
    val cachedData: LiveData<String>
    suspend fun fetchNewData()
}