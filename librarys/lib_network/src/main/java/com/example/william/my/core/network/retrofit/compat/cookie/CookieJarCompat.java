package com.example.william.my.core.network.retrofit.compat.cookie;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class CookieJarCompat {

    public static void cookieJar(Context context, OkHttpClient.Builder builder) {
        builder.cookieJar(buildCookieJar());

        //builder.addInterceptor(new RetrofitInterceptorCookie());
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
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
    }
}
