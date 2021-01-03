package com.example.william.my.core.network.retrofit.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存拦截器
 */
public class RetrofitInterceptorCache implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        String cacheControl = request.cacheControl().toString();
        if (!NetworkUtils.isConnected()) {
            //根据cacheControl设置由缓存还是网络请求
            builder.cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_CACHE : CacheControl.FORCE_NETWORK);
        }

        Response response = chain.proceed(builder.build());
        /*
         * max-age=，缓存时长 60秒
         * max-stale，响应时长，缓存时长 = max-age + ax-stale
         * only-if-cached，仅使用缓存
         * 移除pragma消息头，因为pragma也是控制缓存的一个消息头属性
         */
        if (NetworkUtils.isConnected()) {
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}