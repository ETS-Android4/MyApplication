package com.example.william.my.jet.source;

import androidx.paging.rxjava3.RxPagingSource;

import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.bean.ArticlesBean;
import com.example.william.my.module.service.NetworkService;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataSource extends RxPagingSource<Integer, ArticlesBean.DataBean.ArticleBean> {

    @NotNull
    @Override
    public Single<LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>> loadSingle(@NotNull LoadParams<Integer> loadParams) {

        // Start refresh at page 1 if undefined.
        int nextPageNumber = loadParams.getKey();

        return RetrofitUtils.buildApi(NetworkService.class)
                .getArticle(nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(new Function<ArticlesBean, LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>>() {
                    @Override
                    public LoadResult<Integer, ArticlesBean.DataBean.ArticleBean> apply(ArticlesBean articlesBean) throws Throwable {
                        return new LoadResult.Page<>(
                                articlesBean.getData().getDatas(),
                                null,
                                nextPageNumber + 1
                        );
                    }
                })
                .onErrorReturn(new Function<Throwable, LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>>() {
                    @Override
                    public LoadResult<Integer, ArticlesBean.DataBean.ArticleBean> apply(Throwable throwable) throws Throwable {
                        return new LoadResult.Error<>(throwable);
                    }
                });
    }
}