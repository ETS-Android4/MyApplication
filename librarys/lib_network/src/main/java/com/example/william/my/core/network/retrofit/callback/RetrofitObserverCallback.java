package com.example.william.my.core.network.retrofit.callback;

import com.example.william.my.core.network.retrofit.exception.ApiException;

import io.reactivex.rxjava3.annotations.NonNull;

public interface RetrofitObserverCallback<T> {

    void onLoading();

    void onResponse(@NonNull T response);

    void onFailure(@NonNull ApiException e);
}
