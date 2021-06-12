package com.example.william.my.module.jetpack.source;

import androidx.paging.PagingSource;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.api.NetworkService;
import com.example.william.my.module.bean.ArticleBean;
import com.example.william.my.module.bean.ArticleDetailBean;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArticlePagingSource extends RxPagingSource<Integer, ArticleDetailBean> {

    @NonNull
    @Override
    public Single<PagingSource.LoadResult<Integer, ArticleDetailBean>> loadSingle(@NonNull LoadParams<Integer> loadParams) {

        // Start refresh at page 0 if undefined.
        Integer page = loadParams.getKey();
        if (page == null) {
            page = 0;
        }

        return RetrofitUtils.buildApi(NetworkService.class)
                .getArticleList(page)
                .subscribeOn(Schedulers.io())
                .map(new LoadResult())
                .onErrorReturn(new ErrorResult());
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, ArticleDetailBean> pagingState) {
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

        PagingSource.LoadResult.Page<Integer, ArticleDetailBean> anchorPage = pagingState.closestPageToPosition(anchorPosition);
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

    private static class LoadResult implements Function<ArticleBean, PagingSource.LoadResult<Integer, ArticleDetailBean>> {

        @Override
        public PagingSource.LoadResult<Integer, ArticleDetailBean> apply(ArticleBean articleBean) throws Throwable {
            return new PagingSource.LoadResult.Page<>(
                    articleBean.getData().getDatas(),
                    null,// Only paging forward.
                    articleBean.getData().getCurPage());
        }
    }

    private static class ErrorResult implements Function<Throwable, PagingSource.LoadResult<Integer, ArticleDetailBean>> {

        @Override
        public PagingSource.LoadResult<Integer, ArticleDetailBean> apply(Throwable throwable) throws Throwable {
            return new PagingSource.LoadResult.Error<>(throwable);
        }
    }
}