package com.example.william.my.module.sample.api

import com.example.william.my.bean.base.Urls
import com.example.william.my.bean.data.ArticleBean
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.response.RetrofitResponse

import retrofit2.http.GET
import retrofit2.http.Path

interface KtArticleService {

    @GET(Urls.URL_ARTICLE)
    suspend fun getArticle(@Path("page") page: Int): ArticleBean

    @GET(Urls.URL_ARTICLE)
    suspend fun getArticleResponse(@Path("page") page: Int): RetrofitResponse<ArticleDataBean>
}