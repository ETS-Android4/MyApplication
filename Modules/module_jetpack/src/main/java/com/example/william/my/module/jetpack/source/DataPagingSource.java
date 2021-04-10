package com.example.william.my.module.jetpack.source;

import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.bean.ArticleBean;
import com.example.william.my.module.service.NetworkService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataPagingSource extends RxPagingSource<Integer, ArticleBean.DataBean.ArticleDetailBean> {

    @NotNull
    @Override
    public Single<LoadResult<Integer, ArticleBean.DataBean.ArticleDetailBean>> loadSingle(@NotNull LoadParams<Integer> loadParams) {

        // Start refresh at page 0 if undefined.
        Integer page = loadParams.getKey();
        if (page == null) {
            page = 0;
        }

        return RetrofitUtils.buildApi(NetworkService.class)
                .getArticleList(page)
                .subscribeOn(Schedulers.io())
                .map(new toLoadResult())
                .onErrorReturn(new toErrorResult());
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, ArticleBean.DataBean.ArticleDetailBean> pagingState) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, ArticleBean.DataBean.ArticleDetailBean> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey + 1;
        }

        return null;
    }

    private static class toLoadResult implements Function<ArticleBean, LoadResult<Integer, ArticleBean.DataBean.ArticleDetailBean>> {

        @Override
        public LoadResult<Integer, ArticleBean.DataBean.ArticleDetailBean> apply(ArticleBean articleBean) throws Throwable {
            return new LoadResult.Page<>(
                    articleBean.getData().getDatas(),
                    null,// Only paging forward.
                    articleBean.getData().getCurPage());
        }
    }

    private static class toErrorResult implements Function<Throwable, LoadResult<Integer, ArticleBean.DataBean.ArticleDetailBean>> {

        @Override
        public LoadResult<Integer, ArticleBean.DataBean.ArticleDetailBean> apply(Throwable throwable) throws Throwable {
            return new LoadResult.Error<>(throwable);
        }
    }
}