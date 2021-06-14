package com.example.william.my.module.sample.api

import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDataBean
import com.example.william.my.module.sample.retrofit.response.KtRetrofitResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface KtArticleService {

    @GET(Urls.URL_ARTICLE)
    suspend fun getArticle(@Path("page") page: Int): ArticleBean

    @GET(Urls.URL_ARTICLE)
    suspend fun getArticleResponse(@Path("page") page: Int): KtRetrofitResponse<ArticleDataBean>
}