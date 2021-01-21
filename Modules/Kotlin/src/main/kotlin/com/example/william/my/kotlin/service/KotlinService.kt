package com.example.william.my.kotlin.service

import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 只有被 suspend 修饰的方法，才可以在协程中调用。
 */
interface KotlinService {

    // 提供挂起功能的网络请求接口
    // Interface that provides a way to make network requests with suspend functions
    @POST(Urls.article)
    suspend fun getArticles(@Path("page") page: Int): ArticlesBean
}