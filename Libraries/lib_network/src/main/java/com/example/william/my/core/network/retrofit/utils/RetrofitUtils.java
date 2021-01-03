package com.example.william.my.core.network.retrofit.utils;

import com.example.william.my.core.network.retrofit.body.CountingRequestBody;
import com.example.william.my.core.network.retrofit.callback.RetrofitCallback;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.function.HttpResultFunction;
import com.example.william.my.core.network.retrofit.helper.RetrofitHelper;
import com.example.william.my.core.network.retrofit.listener.RetrofitRequestListener;
import com.example.william.my.core.network.retrofit.observer.RetrofitObserver;

import java.io.File;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetrofitUtils {

    public static String getBasUrl(String url) {
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

    public static <T> T buildApi(Class<T> service) {
        return RetrofitHelper
                .getInstance()
                .baseUrl("https://www.wanandroid.com/")
                .build()
                .create(service);
    }

    public static <T> T buildApi(String baseUrl, Class<T> service) {
        return RetrofitHelper
                .getInstance()
                .baseUrl(baseUrl)
                .build()
                .create(service);
    }

    public static MultipartBody.Part buildMultipart(String key, File file, RetrofitRequestListener listener) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(file));
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, listener);
        return MultipartBody.Part.createFormData(key, file.getName(), countingRequestBody);
    }

    public static <T> Observable<T> buildObs(Observable<T> observable) {
        return observable
                //.map(new ServerResultFunction<T>())
                .onErrorResumeNext(new HttpResultFunction<T>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * ViewModel -> LiveDataCallback
     *
     * @param callback LiveDataCallback
     */
    public static <T> void buildLiveData(Observable<T> observable, final RetrofitCallback<T> callback) {
        observable
                .onErrorResumeNext(new HttpResultFunction<T>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RetrofitObserver<T>() {

                    @Override
                    public void onResponse(T response) {
                        callback.onResponse(response);
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        callback.onFailure(e);
                    }
                });
    }
}
