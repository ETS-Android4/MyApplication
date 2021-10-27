package com.example.william.my.core.okhttp.interceptor;

import androidx.annotation.NonNull;

import com.example.william.my.core.okhttp.body.ResponseProgressBody;
import com.example.william.my.core.okhttp.listener.ResponseProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载进度
 */
public class InterceptorProgress implements Interceptor {

    private final ResponseProgressListener listener;

    public InterceptorProgress(ResponseProgressListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .body(new ResponseProgressBody(request.url().toString(), response.body(), listener))
                .build();
    }
}
