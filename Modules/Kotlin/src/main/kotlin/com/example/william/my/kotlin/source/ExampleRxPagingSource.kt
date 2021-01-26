package com.example.william.my.kotlin.source

import androidx.paging.rxjava3.RxPagingSource
import com.example.william.my.kotlin.service.KotlinApi
import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ExampleRxPagingSource : RxPagingSource<Int, ArticlesBean.DataBean.ArticleBean>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ArticlesBean.DataBean.ArticleBean>> {

        // 如果未定义，从0开始刷新
        // Start refresh at page 0 if undefined.
        val nextPageNumber = params.key ?: 0

        return buildApi().getArticlesSingle(nextPageNumber)
            .subscribeOn(Schedulers.io())
            .map(ReturnLoadResult())
            .onErrorReturn(ReturnError())
    }

    private class ReturnLoadResult :
        Function<ArticlesBean, LoadResult<Int, ArticlesBean.DataBean.ArticleBean>> {
        override fun apply(t: ArticlesBean): LoadResult<Int, ArticlesBean.DataBean.ArticleBean> {
            return LoadResult.Page(
                data = t.data.datas,
                prevKey = null, // Only paging forward.
                nextKey = t.data.curPage
            )
        }
    }

    private class ReturnError :
        Function<Throwable, LoadResult<Int, ArticlesBean.DataBean.ArticleBean>> {
        override fun apply(throwable: Throwable): LoadResult<Int, ArticlesBean.DataBean.ArticleBean> {
            return LoadResult.Error(throwable)
        }
    }

    private fun buildApi(): KotlinApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(Urls.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit.create(KotlinApi::class.java)
    }
}