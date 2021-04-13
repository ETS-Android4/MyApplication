package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.core.network.retrofit.response.RetrofitResponse
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDataBean
import kotlinx.coroutines.flow.Flow

interface KtArticleDataSource {
    fun getArticleBean(): LiveData<ArticleBean>
    val article: LiveData<ArticleBean>
    suspend fun fetchNewData()
}