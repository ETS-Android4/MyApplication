package com.example.william.my.core.retrofit.callback;

import com.example.william.my.core.retrofit.exception.ApiException;

import io.reactivex.rxjava3.annotations.NonNull;

public interface RetrofitObserverCallback<T> {

    /**
     * onLoading
     */
    void onLoading();

    /**
     * onResponse
     *
     * @param response
     */
    void onResponse(@NonNull T response);

    /**
     * onFailure
     *
     * @param e
     */
    void onFailure(@NonNull ApiException e);
}
