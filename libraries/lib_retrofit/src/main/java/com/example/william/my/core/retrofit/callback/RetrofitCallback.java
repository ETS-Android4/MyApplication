package com.example.william.my.core.retrofit.callback;

import com.example.william.my.core.retrofit.base.ResponseCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.exception.ExceptionHandler;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 处理基本逻辑
 * onErrorResumeNext -> ApiException
 * <p>
 * onSuccess : T -> T
 * <p>
 * io.reactivex.rxjava3.core.SingleObserver
 */
public abstract class RetrofitCallback<T> implements SingleObserver<T>, ResponseCallback<T> {

    private Disposable disposable;

    public RetrofitCallback() {
        onLoading();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }

    @Override
    public void onSuccess(@NonNull T t) {
        onResponse(t);

        dispose();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof ApiException) {
            onFailure((ApiException) e);
        } else {
            onFailure(new ApiException(e, ExceptionHandler.ERROR.UNKNOWN));
        }

        dispose();
    }

    @Override
    public void onLoading() {

    }

    private void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
