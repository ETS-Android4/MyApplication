package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.retrofit.ArticleBean
import com.example.william.my.retrofit.ArticleDataBean

interface KtArticleDataSource {
    val article: LiveData<com.example.william.my.retrofit.ArticleBean>
    suspend fun fetchNewData()
    suspend fun loadMoreData()
    val articleResponse: LiveData<RetrofitResponse<com.example.william.my.retrofit.ArticleDataBean>>
    suspend fun fetchNewDataResponse()
    suspend fun loadMoreDataResponse()
}