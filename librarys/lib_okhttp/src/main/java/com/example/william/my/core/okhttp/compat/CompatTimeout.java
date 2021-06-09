package com.example.william.my.core.okhttp.compat;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class CompatTimeout {

    public static void setTimeOut(@NonNull OkHttpClient.Builder builder, long timeout) {
        //不允许失败重试
        builder.retryOnConnectionFailure(false);
        //设置连接超时时间
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        //设置写的超时时间
        builder.writeTimeout(timeout, TimeUnit.SECONDS);
        //设置读取超时时间
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        //设置调用超时时间
        builder.callTimeout(timeout, TimeUnit.SECONDS);
    }
}
