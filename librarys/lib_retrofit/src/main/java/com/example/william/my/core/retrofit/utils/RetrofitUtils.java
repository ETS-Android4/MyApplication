package com.example.william.my.core.retrofit.utils;

import androidx.annotation.NonNull;

import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.function.HttpResultFunction;
import com.example.william.my.core.retrofit.helper.RetrofitHelper;
import com.example.william.my.core.retrofit.observer.RetrofitResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RetrofitUtils {

    public static String getBaseUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    @NonNull
    public static <T> T buildApi(Class<T> api) {
        return RetrofitHelper
                .getInstance()
                .baseUrl("https://www.wanandroid.com/")
                .build()
                .create(api);
    }

    @NonNull
    public static <T> T buildApi(String baseUrl, Class<T> api) {
        return RetrofitHelper
                .getInstance()
                .baseUrl(baseUrl)
                .build()
                .create(api);
    }

    public static <T> void buildObservable(Observable<T> observable, final RetrofitResponseCallback<T> callback) {
        observable
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RetrofitResponse<T>() {
                    @Override
                    public void onResponse(@NonNull T response) {
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NonNull ApiException e) {
                        callback.onFailure(e);
                    }
                });
    }

    /**
     * ViewModel -> LiveDataCallback
     *
     * @param callback LiveDataCallback(需要RetrofitResponse<Bean>格式数据)
     */
    public static <T> void buildLiveData(@NonNull Observable<T> observable, final RetrofitResponseCallback<T> callback) {
        observable
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RetrofitResponse<T>() {
                    @Override
                    public void onResponse(@NonNull T response) {
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(@NonNull ApiException e) {
                        callback.onFailure(e);
                    }
                });
    }
}
