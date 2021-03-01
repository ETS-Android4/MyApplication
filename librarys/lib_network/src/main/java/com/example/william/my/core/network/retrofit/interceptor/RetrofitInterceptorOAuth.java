package com.example.william.my.core.network.retrofit.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitInterceptorOAuth implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "token")
                .build();
        return chain.proceed(request);
    }
}
