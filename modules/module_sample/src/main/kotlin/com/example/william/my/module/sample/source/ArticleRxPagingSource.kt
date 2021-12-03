package com.example.william.my.module.sample.source

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.william.my.bean.api.NetworkService
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticleRxPagingSource : RxPagingSource<Int, ArticleDetailBean>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ArticleDetailBean>> {

        // 如果未定义，从0开始刷新
        // Start refresh at page 0 if undefined.
        val nextPageNumber = params.key ?: 0

        val api = RetrofitUtils.buildApi(NetworkService::class.java)

        return api.getArticleResponse(nextPageNumber)
            .subscribeOn(Schedulers.io())
            .map(ReturnLoadResult())
            .onErrorReturn(ReturnError())
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

    private class ReturnLoadResult :
        Function<RetrofitResponse<ArticleDataBean>, LoadResult<Int, ArticleDetailBean>> {
        override fun apply(t: RetrofitResponse<ArticleDataBean>): LoadResult<Int, ArticleDetailBean> {
            return LoadResult.Page(
                data = t.data.datas,
                prevKey = null, // Only paging forward.
                nextKey = t.data.curPage
            )
        }
    }

    private class ReturnError : Function<Throwable, LoadResult<Int, ArticleDetailBean>> {
        override fun apply(throwable: Throwable): LoadResult<Int, ArticleDetailBean> {
            return LoadResult.Error(throwable)
        }
    }
}