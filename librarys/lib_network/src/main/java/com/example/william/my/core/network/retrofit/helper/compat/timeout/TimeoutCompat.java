package com.example.william.my.core.network.retrofit.helper.compat.timeout;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.base.RxRetrofitConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class TimeoutCompat {

    public static void setTimeOut(@NonNull OkHttpClient.Builder builder) {
        //不允许失败重试
        builder.retryOnConnectionFailure(true);
        //设置连接超时时间
        builder.connectTimeout(RxRetrofitConfig.connectTimeout, TimeUnit.SECONDS);
        //设置写的超时时间
        builder.writeTimeout(RxRetrofitConfig.writeTimeout, TimeUnit.SECONDS);
        //设置读取超时时间
        builder.readTimeout(RxRetrofitConfig.readTimeout, TimeUnit.SECONDS);
        //设置调用超时时间
        builder.callTimeout(RxRetrofitConfig.callTimeout, TimeUnit.SECONDS);
    }
}
