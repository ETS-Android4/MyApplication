package com.example.william.my.module.sample.api;

import com.example.william.my.core.network.retrofit.api.Header;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.sample.bean.ArticleBean;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ArticleService {

    @GET(Urls.article)
    Observable<RetrofitResponse<ArticleBean>> getArticleList(@Path("page") int page);

    @Headers({Header.RETROFIT_CACHE_ALIVE_SECOND + ":" + 10})
    @GET(Urls.article)
    Observable<RetrofitResponse<ArticleBean>> getArticleListCache(@Path("page") int page);
}
