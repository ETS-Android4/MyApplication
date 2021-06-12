package com.example.william.my.core.okhttp.interceptor;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.william.my.core.okhttp.base.Header;
import com.example.william.my.core.okhttp.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存拦截器
 */
public class InterceptorCache implements Interceptor {

    private final Context mContext;

    public InterceptorCache(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        if (!TextUtils.equals(request.method(), "GET")) {
            return chain.proceed(request);
        }

        List<String> headers = request.headers(Header.RETROFIT_CACHE_ALIVE_SECOND);
        if (headers.size() == 0) {
            return chain.proceed(request);
        }

        int age = Integer.parseInt(headers.get(0));

        Request requestCached = buildRequest(request, age);

        return buildResponse(chain.proceed(requestCached), age);
    }

    /**
     * 设置由缓存还是网络请求
     */
    @NonNull
    private Request buildRequest(@NonNull Request request, int age) {
        Request.Builder builder = request.newBuilder();
        if (NetworkUtils.isConnected(mContext)) {
            if (age <= 0) {
                return builder
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else {
                return builder
                        .cacheControl(new CacheControl.Builder().maxAge(age, TimeUnit.SECONDS).build())
                        .build();
            }
        } else {
            return builder
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
    }

    /**
     * max-age=，缓存时长 60秒
     * max-stale，响应时长，缓存时长 = max-age + max-stale
     * only-if-cached，仅使用缓存
     * 移除pragma消息头，因为pragma也是控制缓存的一个消息头属性
     */
    @NonNull
    private Response buildResponse(@NonNull Response response, int age) {
        Response.Builder builder = response.newBuilder();
        if (NetworkUtils.isConnected(mContext)) {
            if (age <= 0) {
                return builder
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .build();
            } else {
                return builder
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + age)
                        .build();
            }
        } else {
            return builder
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + Integer.MAX_VALUE)
                    .build();
        }
    }

}