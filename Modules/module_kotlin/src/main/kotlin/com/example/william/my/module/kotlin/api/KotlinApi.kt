package com.example.william.my.module.kotlin.api

import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 只有被 suspend 修饰的方法，才可以在协程中调用。
 * 1. Retrofit对于suspend方法，相当于定义了一个非suspend方法，并且suspend方法后面的返回值对象T，实际上相当于Call<T>对象
 * 2. suspend方法的执行相当于执行了Call<T>对象的Call.enqueue(callback)
 */
interface KotlinApi {

    // 提供挂起功能的网络请求接口
    // Interface that provides a way to make network requests with suspend functions
    @GET(Urls.article)
    suspend fun getArticles(@Path("page") page: Int): ArticlesBean

    @GET(Urls.article)
    fun getArticlesSingle(@Path("page") page: Int): Single<ArticlesBean>
}