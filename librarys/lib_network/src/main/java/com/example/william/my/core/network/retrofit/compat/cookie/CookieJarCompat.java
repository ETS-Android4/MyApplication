package com.example.william.my.core.network.retrofit.compat.cookie;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.cookiejar.ClearableCookieJar;
import com.example.william.my.core.network.retrofit.cookiejar.PersistentCookieJar;
import com.example.william.my.core.network.retrofit.cookiejar.cache.SetCookieCache;
import com.example.william.my.core.network.retrofit.cookiejar.persist.SharedPrefsCookiePersist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class CookieJarCompat {

    public static void cookieJar(Context context, OkHttpClient.Builder builder) {
        //builder.addInterceptor(new RetrofitInterceptorCookie());
        ClearableCookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(), new SharedPrefsCookiePersist(context));
        //builder.cookieJar(cookieJar);
        builder.cookieJar(buildCookieJar());
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
