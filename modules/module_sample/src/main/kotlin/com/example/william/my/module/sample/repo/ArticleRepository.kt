package com.example.william.my.module.sample.repo

import com.example.william.my.bean.api.NetworkService
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.core.retrofit.utils.RetrofitUtils

class ArticleRepository {

    suspend fun getArticle(page: Int): RetrofitResponse<ArticleDataBean> {
        val api = RetrofitUtils.buildApi(NetworkService::class.java)
        return api.getArticleSuspend(page)
    }
}