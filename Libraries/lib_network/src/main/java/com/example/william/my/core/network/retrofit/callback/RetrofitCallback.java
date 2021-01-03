package com.example.william.my.core.network.retrofit.callback;

import com.example.william.my.core.network.retrofit.exception.ApiException;

public interface RetrofitCallback<T> {

    void onResponse(T response);

    void onFailure(ApiException e);
}
