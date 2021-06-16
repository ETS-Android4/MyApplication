package com.example.william.my.core.retrofit.observer;

import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.exception.ExceptionHandler;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 处理基本逻辑
 * onErrorResumeNext -> ApiException
 * <p>
 * io.reactivex.rxjava3.core.Observer
 */
public abstract class RetrofitObserver<T> implements Observer<T>, RetrofitResponseCallback<T> {

    private Disposable disposable;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        onLoading();
    }

    @Override
    public void onNext(@NonNull T t) {
        onResponse(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof ApiException) {
            onFailure((ApiException) e);
        } else {
            onFailure(new ApiException(e, ExceptionHandler.ERROR.UNKNOWN));
        }

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onLoading() {

    }
}
