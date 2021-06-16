package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDataBean

interface KtArticleDataSource {
    val article: LiveData<ArticleBean>
    suspend fun fetchNewData()
    suspend fun loadMoreData()
    val articleData: LiveData<RetrofitResponse<ArticleDataBean>>
    suspend fun fetchNewDataResponse()
    suspend fun loadMoreDataResponse()
}