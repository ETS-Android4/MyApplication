package com.example.william.my.core.network.retrofit.utils;

import com.example.william.my.core.network.retrofit.api.Api;
import com.example.william.my.core.network.retrofit.builder.RetrofitBuilder;
import com.example.william.my.core.network.retrofit.function.HttpResultFunction;
import com.example.william.my.core.network.retrofit.function.ServerResultFunction;
import com.example.william.my.core.network.retrofit.helper.RetrofitHelper;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.google.gson.JsonElement;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
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

    private Api buildApi() {
        return RetrofitHelper
                .getInstance()
                .baseUrl(RetrofitUtils.getBasUrl(builder.getApi()))
                .build()
                .create(Api.class);
    }

    /**
     * RetrofitConverterFactory & RetrofitResponseBodyConverter
     */
    public Observable<RetrofitResponse<T>> createObservable() {
        Observable<RetrofitResponse<JsonElement>> response = null;
        switch (builder.getMethod()) {
            case GET:
                response = buildApi().get(builder.getApi(), builder.getHeader(), builder.getParameter());
                break;
            case POST:
                if (!builder.hasBody()) {
                    response = buildApi().post(builder.getApi(), builder.getHeader(), builder.getParameter());
                } else {
                    RequestBody body;
                    if (builder.getBodyString() == null || builder.getBodyString().equals("")) {
                        body = builder.getBodyForm().build();
                    } else {
                        String mediaType = builder.isJson() ? "application/json; charset=utf-8" : "text/plain;charset=utf-8";
                        body = RequestBody.create(MediaType.parse(mediaType), builder.getBodyString());
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
        }
        if (response == null) return null;

        Observable<RetrofitResponse<T>> observable = response.map(new ServerResultFunction<T>());

        if (builder.getTransformer() != null) {
            observable = observable.compose(builder.getTransformer());//compose 操作符 介于 map onErrorResumeNext 之间
        }

        observable = observable.onErrorResumeNext(new HttpResultFunction<RetrofitResponse<T>>());

        return observable
                .subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                //.subscribeOn(Schedulers.from(Executors.newFixedThreadPool(3)))//指定线程池
                .observeOn(AndroidSchedulers.mainThread());//请求完成后在主线程更显UI
    }
}
