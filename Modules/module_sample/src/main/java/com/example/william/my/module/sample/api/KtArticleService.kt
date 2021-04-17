package com.example.william.my.module.sample.api

import com.example.william.my.core.network.retrofit.response.RetrofitResponse
import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDetailBean
import retrofit2.http.GET
import retrofit2.http.Path

interface KtArticleService {

    @GET(Urls.article)
    suspend fun getArticle(@Path("page") page: Int): ArticleBean

    @GET(Urls.article)
    suspend fun getArticleResponse(@Path("page") page: Int): RetrofitResponse<ArticleDetailBean>
}