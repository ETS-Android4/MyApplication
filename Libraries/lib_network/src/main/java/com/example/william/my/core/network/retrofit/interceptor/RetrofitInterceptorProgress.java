package com.example.william.my.core.network.retrofit.interceptor;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.body.ProgressResponseBody;
import com.example.william.my.core.network.retrofit.listener.RetrofitResponseListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载进度
 */
public class RetrofitInterceptorProgress implements Interceptor {

    private final RetrofitResponseListener listener;

    public RetrofitInterceptorProgress(RetrofitResponseListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .body(new ProgressResponseBody(request.url().toString(), response.body(), listener))
                .build();
    }
}
