package com.example.william.my.module.sample.api

import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticleBean
import retrofit2.http.GET
import retrofit2.http.Path

interface KtArticleService {

    @GET(Urls.article)
    suspend fun getArticle(@Path("page") page: Int): ArticleBean
}