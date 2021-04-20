package com.example.william.my.core.network.retrofit.utils;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.body.CountingRequestBody;
import com.example.william.my.core.network.retrofit.callback.RetrofitCallback;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.function.HttpResultFunction;
import com.example.william.my.core.network.retrofit.helper.RetrofitHelper;
import com.example.william.my.core.network.retrofit.listener.RetrofitRequestListener;
import com.example.william.my.core.network.retrofit.observer.RetrofitObserver;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetrofitUtils {

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

    @NonNull
    public static MultipartBody.Part buildMultipart(String key, File file, RetrofitRequestListener listener) {
        RequestBody requestBody = RequestBody.Companion.create(file, MediaType.parse("multipart/form-data"));
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, listener);
        return MultipartBody.Part.createFormData(key, file.getName(), countingRequestBody);
    }

    @NonNull
    public static <T> Observable<T> buildObs(Observable<T> observable) {
        return observable
                //.map(new ServerResultFunction<T>())
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * ViewModel -> LiveDataCallback
     *
     * @param callback LiveDataCallback(需要RetrofitResponse<Bean>格式数据)
     */
    public static <T> void buildLiveData(@NonNull Observable<T> observable, final RetrofitCallback<T> callback) {
        observable
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RetrofitObserver<T>() {

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
