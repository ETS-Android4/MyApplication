package com.example.william.my.core.network.retrofit.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.base.RxRetrofitConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitInterceptorCookie implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String cookie = getCookie(request.url().toString(), request.url().host());
        if (cookie != null && !TextUtils.isEmpty(cookie)) {
            //添加cookie
            builder.addHeader("Cookie", cookie);
        }
        //========================================
        Response response = chain.proceed(builder.build());
        Headers headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            if (!response.headers("set-cookie").isEmpty()) {
                //存储cookie
                List<String> cookies = response.headers("set-cookie");
                saveCookie(request.url().toString(), request.url().host(), encodeCookie(cookies));
            }
        }
        return response;
    }

    /**
     * 整合cookie为唯一字符串
     */
    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if (set.contains(s)) continue;
                set.add(s);
            }
        }
        for (String cookie : set) {
            sb.append(cookie).append(";");
        }
        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }
        return sb.toString();
    }

    /**
     * 保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
     * 这样能使得该cookie的应用范围更广
     */
    private void saveCookie(String url, String domain, String cookies) {
        SharedPreferences sp = RxRetrofitConfig.getApp().getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url is null.");
        } else {
            editor.putString(url, cookies);
        }
        if (!TextUtils.isEmpty(domain)) {
            editor.putString(domain, cookies);
        }
        editor.apply();
    }

    private String getCookie(String url, String domain) {
        SharedPreferences sp = RxRetrofitConfig.getApp().getSharedPreferences("cookie", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(url) && sp.contains(url) && !TextUtils.isEmpty(sp.getString(url, ""))) {
            return sp.getString(url, "");
        }
        if (!TextUtils.isEmpty(domain) && sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {
            return sp.getString(domain, "");
        }
        return null;
    }
}
