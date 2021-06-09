package com.example.william.my.core.okhttp.compat;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.okhttp.interceptor.InterceptorLogging;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * HttpLoggingInterceptor
 */
public class CompatLogging {

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
        builder.addInterceptor(new InterceptorLogging(tag));
    }

    public static void setLogBody(@NonNull OkHttpClient.Builder builder, String tag) {
        builder.addInterceptor(new InterceptorLogging(tag)
                .setLevel(InterceptorLogging.Level.BODY));
    }

    private static void showLog(String tag, @NonNull String msg) {
        int maxLength = 2 * 1024;
        while (msg.length() > maxLength) {
            Log.i(tag, msg.substring(0, maxLength));
            msg = msg.substring(maxLength);
        }
        Log.i(tag, msg);
    }
}
