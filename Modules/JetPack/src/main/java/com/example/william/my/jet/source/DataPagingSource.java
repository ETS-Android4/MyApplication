package com.example.william.my.jet.source;

import androidx.paging.rxjava3.RxPagingSource;

import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.bean.ArticlesBean;
import com.example.william.my.module.service.NetworkService;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataPagingSource extends RxPagingSource<Integer, ArticlesBean.DataBean.ArticleBean> {

    @NotNull
    @Override
    public Single<LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>> loadSingle(@NotNull LoadParams<Integer> loadParams) {

        // Start refresh at page 0 if undefined.
        Integer page = loadParams.getKey();
        if (page == null) {
            page = 0;
        }

        return RetrofitUtils.buildApi(NetworkService.class)
                .getArticle(page)
                .subscribeOn(Schedulers.io())
                .map(new toLoadResult(page))
                .onErrorReturn(new toErrorResult());
    }

    private static class toLoadResult implements Function<ArticlesBean, LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>> {

        private final Integer page;

        public toLoadResult(Integer page) {
            this.page = page;
        }

        @Override
        public LoadResult<Integer, ArticlesBean.DataBean.ArticleBean> apply(ArticlesBean articlesBean) throws Throwable {
            return new LoadResult.Page<>(
                    articlesBean.getData().getDatas(),
                    null,// Only paging forward.
                    page + 1);
        }
    }

    private static class toErrorResult implements Function<Throwable, LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>> {

        @Override
        public LoadResult<Integer, ArticlesBean.DataBean.ArticleBean> apply(Throwable throwable) throws Throwable {
            return new LoadResult.Error<>(throwable);
        }
    }
}