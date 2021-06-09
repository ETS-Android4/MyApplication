package com.example.william.my.core.okhttp.compat;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.william.my.core.okhttp.interceptor.InterceptorCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class CompatCookieJar {

    public static void cookieJar(@NonNull OkHttpClient.Builder builder) {
        builder.cookieJar(buildCookieJar());
    }

    public static void cookieJar(Context context, @NonNull OkHttpClient.Builder builder) {
        builder.addInterceptor(new InterceptorCookie(context));
    }

    private static CookieJar buildCookieJar() {
        return new CookieJar() {

            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @NonNull
            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        };
    }
}
