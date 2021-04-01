package com.example.william.my.core.network.retrofit.compat.log;

import android.util.Log;

import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorLogging;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * HttpLoggingInterceptor
 * implementation "com.squareup.okhttp3:logging-interceptor:3.9.0"
 */
public class LogCompat {

    public static void setLog(OkHttpClient.Builder builder) {
        /*
         * 添加拦截器
         * addInterceptor,在response被调用一次
         * addNetworkInterceptor,在request和response是分别被调用一次
         */
        builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("OkHttp", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    public static void setLogBasic(OkHttpClient.Builder builder) {
        builder.addInterceptor(new RetrofitInterceptorLogging());
    }

    public static void setLogBody(OkHttpClient.Builder builder) {
        builder.addInterceptor(new RetrofitInterceptorLogging()
                .setLevel(RetrofitInterceptorLogging.Level.BODY));
    }

}
