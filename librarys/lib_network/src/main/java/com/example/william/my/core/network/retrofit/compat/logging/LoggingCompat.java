package com.example.william.my.core.network.retrofit.compat.logging;

import android.util.Log;

import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorLogging;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * HttpLoggingInterceptor
 * implementation "com.squareup.okhttp3:logging-interceptor:3.9.0"
 */
public class LoggingCompat {

    private static final String TAG = "OkHttp";

    public static void setLog(OkHttpClient.Builder builder) {
        setLog(builder, TAG);
    }

    public static void setLog(OkHttpClient.Builder builder, String tag) {
        /*
         * 添加拦截器
         * addInterceptor,在response被调用一次
         * addNetworkInterceptor,在request和response是分别被调用一次
         */
        builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    public static void setLogBasic(OkHttpClient.Builder builder) {
        setLogBasic(builder, TAG);
    }

    public static void setLogBasic(OkHttpClient.Builder builder, String tag) {
        builder.addInterceptor(new RetrofitInterceptorLogging(tag));
    }

    public static void setLogBody(OkHttpClient.Builder builder) {
        setLogBody(builder, TAG);
    }

    public static void setLogBody(OkHttpClient.Builder builder, String tag) {
        builder.addInterceptor(new RetrofitInterceptorLogging(tag)
                .setLevel(RetrofitInterceptorLogging.Level.BODY));
    }

}
