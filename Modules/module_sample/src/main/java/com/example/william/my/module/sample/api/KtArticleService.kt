package com.example.william.my.module.sample.api

import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.retrofit.base.Urls
import com.example.william.my.retrofit.ArticleBean
import com.example.william.my.retrofit.ArticleDataBean
import retrofit2.http.GET
import retrofit2.http.Path

interface KtArticleService {

    @GET(Urls.URL_ARTICLE)
    suspend fun getArticle(@Path("page") page: Int): com.example.william.my.retrofit.ArticleBean

    @GET(Urls.URL_ARTICLE)
    suspend fun getArticleResponse(@Path("page") page: Int): RetrofitResponse<com.example.william.my.retrofit.ArticleDataBean>
}