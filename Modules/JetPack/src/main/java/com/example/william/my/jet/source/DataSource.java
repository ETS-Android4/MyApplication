package com.example.william.my.jet.source;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.jet.repository.Repository;
import com.example.william.my.module.bean.BannerBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import kotlin.coroutines.Continuation;

public class DataSource extends PagingSource<Integer, BannerBean> {

    @Nullable
    @Override
    public Object load(@NotNull LoadParams<Integer> loadParams, @NotNull Continuation<? super LoadResult<Integer, BannerBean>> continuation) {
        return null;
    }

    public LoadResult<Integer, BannerBean> load(@NotNull LoadParams<Integer> loadParams) {

        LiveData<RetrofitResponse<List<BannerBean>>> data = Repository.getInstance().bannerBean();

        if (data.getValue().getData().size() > 0) {
            return new LoadResult.Page<>(data.getValue().getData(), null, null);
        } else {
            return null;
        }
    }
}