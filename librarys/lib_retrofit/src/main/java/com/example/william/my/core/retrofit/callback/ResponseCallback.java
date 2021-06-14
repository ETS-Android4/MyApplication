package com.example.william.my.core.retrofit.callback;

import com.example.william.my.core.retrofit.response.RetrofitResponse;

public abstract class ResponseCallback<Bean> implements RetrofitResponseCallback<RetrofitResponse<Bean>> {

    public ResponseCallback() {
        onLoading();
    }

    @Override
    public void onLoading() {

    }
}
