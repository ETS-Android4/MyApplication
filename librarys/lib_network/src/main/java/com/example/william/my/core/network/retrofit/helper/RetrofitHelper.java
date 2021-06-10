package com.example.william.my.core.network.retrofit.helper;

import com.example.william.my.core.network.retrofit.converter.RetrofitConverterFactory;
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
    private static OkHttpClient.Builder okhttpClient;

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
        okhttpClient = OkHttpHelper.getInstance();
    }

    public RetrofitHelper baseUrl(String baseUrl) {
        retrofit.baseUrl(baseUrl);
        return this;
    }

    public RetrofitHelper setDownloadListener(RetrofitResponseListener downloadListener) {
        okhttpClient.addInterceptor(new RetrofitInterceptorProgress(downloadListener));
        return this;
    }

    public Retrofit build() {
        return build(okhttpClient.build());
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
                // 标准类型转换器，防止上传图文的时候带引号
                .addConverterFactory(ScalarsConverterFactory.create())
                // 解析工厂类
                .addConverterFactory(factory)
                // 支持RxJava3
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
}
