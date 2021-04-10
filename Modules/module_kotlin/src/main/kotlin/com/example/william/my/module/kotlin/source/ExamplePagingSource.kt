package com.example.william.my.module.kotlin.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.kotlin.api.KotlinApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExamplePagingSource : PagingSource<Int, ArticleBean.DataBean.ArticleDetailBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleBean.DataBean.ArticleDetailBean> {
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

    override fun getRefreshKey(state: PagingState<Int, ArticleBean.DataBean.ArticleDetailBean>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
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