package com.example.william.my.kotlin.service

import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 只有被 suspend 修饰的方法，才可以在协程中调用。
 */
interface KotlinApi {

    // 提供挂起功能的网络请求接口
    // Interface that provides a way to make network requests with suspend functions
    @GET(Urls.article)
    suspend fun getArticles(@Path("page") page: Int): ArticlesBean

    // 提供挂起功能的网络请求接口
    // Interface that provides a way to make network requests with suspend functions
    @GET(Urls.article)
    fun getArticlesSingle(@Path("page") page: Int): Single<ArticlesBean>
}