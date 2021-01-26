package com.example.william.my.jet.source;

import android.util.Log;

import androidx.paging.rxjava3.RxPagingSource;

import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.bean.ArticlesBean;
import com.example.william.my.module.service.NetworkService;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataPagingSource extends RxPagingSource<Integer, ArticlesBean.DataBean.ArticleBean> {

    @NotNull
    @Override
    public Single<LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>> loadSingle(@NotNull LoadParams<Integer> loadParams) {

        // Start refresh at page 0 if undefined.
        Integer page = loadParams.getKey();
        if (page == null) {
            page = 0;
        }
        Log.e("TAG", page + "");

        return RetrofitUtils.buildApi(NetworkService.class)
                .getArticle(page)
                .subscribeOn(Schedulers.io())
                .map(new toLoadResult())
                .onErrorReturn(new toErrorResult());
    }

    private static class toLoadResult implements Function<ArticlesBean, LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>> {

        @Override
        public LoadResult<Integer, ArticlesBean.DataBean.ArticleBean> apply(ArticlesBean articlesBean) throws Throwable {
            Log.e("TAG", new Gson().toJson(articlesBean));
            return new LoadResult.Page<>(
                    articlesBean.getData().getDatas(),
                    null,// Only paging forward.
                    articlesBean.getData().getCurPage());
        }
    }

    private static class toErrorResult implements Function<Throwable, LoadResult<Integer, ArticlesBean.DataBean.ArticleBean>> {

        @Override
        public LoadResult<Integer, ArticlesBean.DataBean.ArticleBean> apply(Throwable throwable) throws Throwable {
            Log.e("TAG", throwable.getMessage());
            return new LoadResult.Error<>(throwable);
        }
    }
}