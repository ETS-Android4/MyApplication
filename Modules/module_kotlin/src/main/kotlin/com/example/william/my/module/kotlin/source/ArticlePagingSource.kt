package com.example.william.my.module.kotlin.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleDetailBean
import com.example.william.my.module.kotlin.api.KotlinApi

class ArticlePagingSource : PagingSource<Int, ArticleDetailBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleDetailBean> {
        return try {
            // 如果未定义，从0开始刷新
            // Start refresh at page 0 if undefined.
            val nextPageNumber = params.key ?: 0

            val api = RetrofitUtils.buildApi(KotlinApi::class.java)

            val response = api.getArticleSuspend(nextPageNumber)

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

    override fun getRefreshKey(state: PagingState<Int, ArticleDetailBean>): Int? {
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
}