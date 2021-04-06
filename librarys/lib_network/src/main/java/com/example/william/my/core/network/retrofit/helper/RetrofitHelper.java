package com.example.william.my.core.network.retrofit.helper;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.core.network.retrofit.converter.RetrofitConverterFactory;
import com.example.william.my.core.network.retrofit.helper.compat.cache.CacheCompat;
import com.example.william.my.core.network.retrofit.helper.compat.cookie.CookieJarCompat;
import com.example.william.my.core.network.retrofit.helper.compat.logging.LoggingCompat;
import com.example.william.my.core.network.retrofit.helper.compat.ssl.HttpsSSLCompat;
import com.example.william.my.core.network.retrofit.helper.compat.timeout.TimeoutCompat;
import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorProgress;
import com.example.william.my.core.network.retrofit.listener.RetrofitResponseListener;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {

    private static RetrofitHelper instance;

    private static Retrofit.Builder retrofit;
    private static OkHttpClient.Builder okHttpClient;

    private static RetrofitResponseListener downloadListener;

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            synchronized (RetrofitHelper.class) {
                if (instance == null) {
                    instance = new RetrofitHelper();
                }
            }
        }
        return instance;
    }

    private RetrofitHelper() {
        retrofit = new Retrofit.Builder();
        okHttpClient = new OkHttpClient.Builder();
    }

    public RetrofitHelper baseUrl(String baseUrl) {
        retrofit.baseUrl(baseUrl);
        return this;
    }

    public RetrofitHelper setDownloadListener(RetrofitResponseListener listener) {
        downloadListener = listener;
        return this;
    }

    public OkHttpClient.Builder okHttpClient() {
        //设置连接使用的HTTP代理。该方法优先于proxySelector，默认代理为空，完全禁用代理使用NO_PROXY
        //okHttpClient.proxy(Proxy.NO_PROXY);

        //设置超时时间
        TimeoutCompat.setTimeOut(okHttpClient);

        //设置缓存
        if (RxRetrofitConfig.setCache)
            CacheCompat.cache(RxRetrofitConfig.getApp(), okHttpClient, RxRetrofitConfig.cacheDir, RxRetrofitConfig.cacheSize);

        //显示log
        if (RxRetrofitConfig.showLogging)
            LoggingCompat.setLog(okHttpClient, RxRetrofitConfig.loggingTag);

        //携带cookie
        CookieJarCompat.cookieJar(okHttpClient);

        //忽略https证书
        HttpsSSLCompat.ignoreSSLForOkHttp(okHttpClient);
        HttpsSSLCompat.ignoreSSLForHttpsURLConnection();

        if (downloadListener != null) {
            okHttpClient.addInterceptor(new RetrofitInterceptorProgress(downloadListener));
        }

        return okHttpClient;
    }

    public Retrofit build() {
        return build(okHttpClient().build());
    }

    /**
     * GsonConverterFactory.create()
     * RetrofitConverterFactory.create()
     */
    public Retrofit build(OkHttpClient okHttpClient) {
        return build(okHttpClient, RetrofitConverterFactory.create());
    }

    public Retrofit build(OkHttpClient okHttpClient, Converter.Factory factory) {
        return retrofit
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create()) //标准类型转换器，防止上传图文的时候带引号
                .addConverterFactory(factory)//解析工厂类
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 支持RxJava
                .build();
    }
}
