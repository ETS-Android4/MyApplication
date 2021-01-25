package com.example.william.my.kotlin.source

import androidx.paging.PagingSource
import com.example.william.my.kotlin.service.KotlinApi
import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExamplePagingSource : PagingSource<Int, ArticlesBean.DataBean.ArticleBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesBean.DataBean.ArticleBean> {
        return try {
            // 如果未定义，从0开始刷新
            // Start refresh at page 0 if undefined.
            val nextPageNumber = params.key ?: 0
            val response = buildApi().getArticles(nextPageNumber)
            LoadResult.Page(
                data = response.data.datas,
                prevKey = null, // Only paging forward.
                nextKey = response.data.curPage
            )
        } catch (e: Exception) {
            // 处理错误，返回 LoadResult.Error()
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            LoadResult.Error(e)
        }
    }

    private fun buildApi(): KotlinApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(Urls.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(KotlinApi::class.java)
    }
}