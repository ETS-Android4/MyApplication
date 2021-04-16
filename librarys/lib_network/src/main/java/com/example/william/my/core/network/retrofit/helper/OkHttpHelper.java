package com.example.william.my.core.network.retrofit.helper;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.core.network.retrofit.helper.compat.cache.CacheCompat;
import com.example.william.my.core.network.retrofit.helper.compat.cookie.CookieJarCompat;
import com.example.william.my.core.network.retrofit.helper.compat.logging.LoggingCompat;
import com.example.william.my.core.network.retrofit.helper.compat.ssl.HttpsSSLCompat;
import com.example.william.my.core.network.retrofit.helper.compat.timeout.TimeoutCompat;

import okhttp3.OkHttpClient;

public class OkHttpHelper {

    private static OkHttpClient.Builder mOkHttpClient;

    public static OkHttpClient.Builder getInstance() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpHelper.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = createBuilder();
                }
            }
        }
        return mOkHttpClient;
    }

    private static OkHttpClient.Builder createBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //设置连接使用的HTTP代理。该方法优先于proxySelector，默认代理为空，完全禁用代理使用NO_PROXY
        //okHttpClient.proxy(Proxy.NO_PROXY);

        //设置超时时间
        TimeoutCompat.setTimeOut(builder);

        //设置缓存
        if (RxRetrofitConfig.setCache)
            CacheCompat.cache(RxRetrofitConfig.getApp(), builder, RxRetrofitConfig.cacheDir, RxRetrofitConfig.cacheSize);

        //显示log
        if (RxRetrofitConfig.showLogging)
            LoggingCompat.setLog(builder, RxRetrofitConfig.loggingTag);

        //携带cookie
        CookieJarCompat.cookieJar(builder);

        //忽略https证书
        HttpsSSLCompat.ignoreSSLForOkHttp(builder);
        HttpsSSLCompat.ignoreSSLForHttpsURLConnection();

        //if (downloadListener != null) {
        //    builder.addInterceptor(new RetrofitInterceptorProgress(downloadListener));
        //}

        return builder;
    }
}
