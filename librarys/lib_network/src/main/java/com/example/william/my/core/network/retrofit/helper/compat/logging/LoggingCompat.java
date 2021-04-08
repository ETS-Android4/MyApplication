package com.example.william.my.core.network.retrofit.helper.compat.logging;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorLogging;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * HttpLoggingInterceptor
 * implementation "com.squareup.okhttp3:logging-interceptor:3.9.0"
 */
public class LoggingCompat {

    public static void setLog(@NonNull OkHttpClient.Builder builder, final String tag) {
        /*
         * 添加拦截器
         * addInterceptor,在response被调用一次
         * addNetworkInterceptor,在request和response是分别被调用一次
         */
        builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                showLog(tag, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    public static void setLogBasic(@NonNull OkHttpClient.Builder builder, String tag) {
        builder.addInterceptor(new RetrofitInterceptorLogging(tag));
    }

    public static void setLogBody(@NonNull OkHttpClient.Builder builder, String tag) {
        builder.addInterceptor(new RetrofitInterceptorLogging(tag)
                .setLevel(RetrofitInterceptorLogging.Level.BODY));
    }

    private static void showLog(String tag, @NonNull String msg) {
        int maxLength = 2 * 1024;
        while (msg.length() > maxLength) {
            Log.e(tag, msg.substring(0, maxLength));
            msg = msg.substring(maxLength);
        }
        Log.e(tag, msg);
    }
}
