package com.example.william.my.core.retrofit;

import androidx.annotation.NonNull;

import com.example.william.my.core.retrofit.api.Api;
import com.example.william.my.core.retrofit.builder.RetrofitBuilder;
import com.example.william.my.core.retrofit.function.HttpResultFunction;
import com.example.william.my.core.retrofit.function.ServerResultFunction;
import com.example.william.my.core.retrofit.helper.RetrofitHelper;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.google.gson.JsonElement;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Http请求类
 */
public class RxRetrofit<T> {

    private final RetrofitBuilder<T> builder;

    public static <T> RetrofitBuilder<T> Builder() {
        return new RetrofitBuilder<>();
    }

    public RxRetrofit(RetrofitBuilder<T> builder) {
        this.builder = builder;
    }

    @NonNull
    private Api buildApi() {
        return RetrofitHelper
                .getInstance()
                .baseUrl(RetrofitUtils.getBaseUrl(builder.getApi()))
                .build()
                .create(Api.class);
    }

    /**
     * RetrofitConverterFactory & RetrofitResponseBodyConverter
     */
    public Single<RetrofitResponse<T>> createResponseSingle() {
        Single<RetrofitResponse<JsonElement>> response = null;
        switch (builder.getMethod()) {
            case GET:
                response = buildApi().get(builder.getApi(), builder.getHeader(), builder.getParameter());
                break;
            case POST:
                if (!builder.hasBody()) {
                    response = buildApi().post(builder.getApi(), builder.getHeader(), builder.getParameter());
                } else {
                    RequestBody body;
                    if (builder.getBodyString() == null || "".equals(builder.getBodyString())) {
                        body = builder.getBodyForm().build();
                    } else {
                        String mediaType = builder.isJson() ? "application/json; charset=utf-8" : "text/plain;charset=utf-8";
                        body = RequestBody.Companion.create(builder.getBodyString(), MediaType.parse(mediaType));
                    }
                    response = buildApi().post(builder.getApi(), builder.getHeader(), body);
                }
                break;
            case PUT:
                response = buildApi().put(builder.getApi(), builder.getHeader(), builder.getParameter());
                break;
            case DELETE:
                response = buildApi().delete(builder.getApi(), builder.getHeader(), builder.getParameter());
                break;
            default:
                break;
        }
        if (response == null) {
            return null;
        }

        Single<RetrofitResponse<T>> single = response.map(new ServerResultFunction<>());

        if (builder.getTransformer() != null) {
            //compose 操作符 介于 map onErrorResumeNext 之间
            single = single.compose(builder.getTransformer());
        }

        single = single.onErrorResumeNext(new HttpResultFunction<>());

        return single
                //请求数据的事件发生在io线程
                .subscribeOn(Schedulers.io())
                //指定线程池
                //.subscribeOn(Schedulers.from(Executors.newFixedThreadPool(3)))
                //请求完成后在主线程更显UI
                .observeOn(AndroidSchedulers.mainThread());
    }
}
