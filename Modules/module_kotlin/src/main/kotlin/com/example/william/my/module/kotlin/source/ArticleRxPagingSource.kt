package com.example.william.my.module.kotlin.source

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import com.example.william.my.retrofit.ArticleBean
import com.example.william.my.retrofit.ArticleDetailBean
import com.example.william.my.module.kotlin.api.KotlinApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticleRxPagingSource : RxPagingSource<Int, com.example.william.my.retrofit.ArticleDetailBean>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, com.example.william.my.retrofit.ArticleDetailBean>> {

        // 如果未定义，从0开始刷新
        // Start refresh at page 0 if undefined.
        val nextPageNumber = params.key ?: 0

        val api = RetrofitUtils.buildApi(KotlinApi::class.java)

        return api.getArticlesSingle(nextPageNumber)
            .subscribeOn(Schedulers.io())
            .map(ReturnLoadResult())
            .onErrorReturn(ReturnError())
    }

    override fun getRefreshKey(state: PagingState<Int, com.example.william.my.retrofit.ArticleDetailBean>): Int? {
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

    private class ReturnLoadResult :
        Function<com.example.william.my.retrofit.ArticleBean, LoadResult<Int, com.example.william.my.retrofit.ArticleDetailBean>> {
        override fun apply(t: com.example.william.my.retrofit.ArticleBean): LoadResult<Int, com.example.william.my.retrofit.ArticleDetailBean> {
            return LoadResult.Page(
                data = t.data.datas,
                prevKey = null, // Only paging forward.
                nextKey = t.data.curPage
            )
        }
    }

    private class ReturnError :
        Function<Throwable, LoadResult<Int, com.example.william.my.retrofit.ArticleDetailBean>> {
        override fun apply(throwable: Throwable): LoadResult<Int, com.example.william.my.retrofit.ArticleDetailBean> {
            return LoadResult.Error(throwable)
        }
    }
}