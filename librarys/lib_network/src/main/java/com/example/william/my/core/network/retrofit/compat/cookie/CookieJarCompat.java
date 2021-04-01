package com.example.william.my.core.network.retrofit.compat.cookie;

import android.content.Context;

import com.example.william.my.core.network.retrofit.cookiejar.ClearableCookieJar;
import com.example.william.my.core.network.retrofit.cookiejar.PersistentCookieJar;
import com.example.william.my.core.network.retrofit.cookiejar.cache.SetCookieCache;
import com.example.william.my.core.network.retrofit.cookiejar.persist.SharedPrefsCookiePersist;

import okhttp3.OkHttpClient;

public class CookieJarCompat {

    public static void cookieJar(Context context, OkHttpClient.Builder builder) {
        //builder.addInterceptor(new RetrofitInterceptorCookie());
        ClearableCookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(), new SharedPrefsCookiePersist(context));
        builder.cookieJar(cookieJar);
    }
}
