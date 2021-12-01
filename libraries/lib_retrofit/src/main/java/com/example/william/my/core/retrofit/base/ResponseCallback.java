package com.example.william.my.core.retrofit.base;

import com.example.william.my.core.retrofit.exception.ApiException;

public interface ResponseCallback<T> {

    /**
     * onLoading
     */
    void onLoading();

    /**
     * onResponse
     *
     * @param response
     */
    void onResponse(T response);

    /**
     * onFailure
     *
     * @param e
     */
    void onFailure(ApiException e);
}
