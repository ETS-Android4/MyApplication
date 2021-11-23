package com.example.william.my.module.sample.api;

import com.example.william.my.bean.base.Urls;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.okhttp.base.Header;
import com.example.william.my.core.retrofit.response.RetrofitResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ArticleService {

    @GET(Urls.URL_ARTICLE)
    Observable<RetrofitResponse<ArticleDataBean>> getArticleList(@Path("page") int page);

    @Headers({Header.RETROFIT_CACHE_ALIVE_SECOND + ":" + 10})
    @GET(Urls.URL_ARTICLE)
    Observable<RetrofitResponse<ArticleDataBean>> getArticleListCache(@Path("page") int page);
}
