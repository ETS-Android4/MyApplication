package com.example.william.my.module.sample.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.bean.data.ArticleDetailBean;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.sample.frame.data.source.TasksDataSource;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * PagingSource
 * java + rxjava
 */
public class ArticleRxPagingSource extends RxPagingSource<Integer, ArticleDetailBean> {

    @NonNull
    private final TasksDataSource mDataSource;

    public ArticleRxPagingSource(@NonNull TasksDataSource mDataSource) {
        this.mDataSource = mDataSource;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, ArticleDetailBean>> loadSingle(
            @NonNull LoadParams<Integer> params) {
        // Start refresh at page 1 if undefined.
        Integer nextPageNumber = params.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }

        return mDataSource.getArticleSingle(nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(this::toLoadResult)
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<Integer, ArticleDetailBean> toLoadResult(
            @NonNull RetrofitResponse<ArticleDataBean> response) {
        return new LoadResult.Page<>(
                response.getData().getDatas(),
                null, // Only paging forward.
                response.getData().getCurPage(),
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, ArticleDetailBean> state) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, ArticleDetailBean> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }
}