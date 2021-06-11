package com.example.william.my.core.retrofit;

import androidx.annotation.NonNull;

import com.example.william.my.core.retrofit.callback.RetrofitObserverCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.function.HttpResultFunction;
import com.example.william.my.core.retrofit.helper.RetrofitHelper;
import com.example.william.my.core.retrofit.observer.RetrofitObserver;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    public static <T> void buildLiveData(@NonNull Observable<T> observable, final RetrofitObserverCallback<T> callback) {
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
