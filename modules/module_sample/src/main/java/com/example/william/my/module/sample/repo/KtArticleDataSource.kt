package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.bean.data.ArticleBean
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.response.RetrofitResponse

interface KtArticleDataSource {
    val article: LiveData<ArticleBean>
    suspend fun fetchNewData()
    suspend fun loadMoreData()
    val articleResponse: LiveData<RetrofitResponse<ArticleDataBean>>
    suspend fun fetchNewDataResponse()
    suspend fun loadMoreDataResponse()
}