package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDataBean
import com.example.william.my.module.sample.retrofit.response.KtRetrofitResponse

interface KtArticleDataSource {
    val article: LiveData<ArticleBean>
    suspend fun fetchNewData()
    suspend fun loadMoreData()
    val articleData: LiveData<KtRetrofitResponse<ArticleDataBean>>
    suspend fun fetchNewDataResponse()
    suspend fun loadMoreDataResponse()
}