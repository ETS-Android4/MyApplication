package com.example.william.my.core.network.retrofit.helper;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.core.network.retrofit.converter.RetrofitConverterFactory;
import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorCache;
import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorLogging;
import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorProgress;
import com.example.william.my.core.network.retrofit.listener.RetrofitResponseListener;
import com.example.william.my.core.network.retrofit.ssl.SSLSocketClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
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

        okHttpClient.retryOnConnectionFailure(true);//允许失败重试

        okHttpClient.connectTimeout(RxRetrofitConfig.connectTimeout, TimeUnit.SECONDS);//设置连接超时时间
        okHttpClient.writeTimeout(RxRetrofitConfig.writeTimeout, TimeUnit.SECONDS);//设置写的超时时间
        okHttpClient.readTimeout(RxRetrofitConfig.readTimeout, TimeUnit.SECONDS);//设置读取超时时间
        okHttpClient.callTimeout(RxRetrofitConfig.callTimeout, TimeUnit.SECONDS);//设置调用超时时间

        setLog(RxRetrofitConfig.showLog);
        setCookie(RxRetrofitConfig.setCookie);

        setCache(RxRetrofitConfig.setCache, RxRetrofitConfig.cacheSize);

        if (downloadListener != null) {
            okHttpClient.addInterceptor(new RetrofitInterceptorProgress(downloadListener));
        }

        //忽略https证书
        okHttpClient.sslSocketFactory(SSLSocketClient.createSSLSocketFactory(), new SSLSocketClient.TrustAllManager());//证书验证
        okHttpClient.hostnameVerifier(new SSLSocketClient.TrustAllHostnameVerifier());//主机验证
        return okHttpClient;
    }

    private void setLog(boolean show) {
        if (show) {
            /*
             * 添加拦截器
             * addInterceptor,在response被调用一次
             * addNetworkInterceptor,在request和response是分别被调用一次
             */
            //retrofit.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            //    @Override
            //    public void log(String message) {
            //        Log.e("OkHttp", message);
            //    }
            //}).setLevel(HttpLoggingInterceptor.Level.BODY));
            okHttpClient.addInterceptor(new RetrofitInterceptorLogging());
        }
    }

    private void setCookie(boolean cookie) {
        if (cookie) {
            /*
             * 携带cookie
             */
            okHttpClient.cookieJar(new CookieJar() {

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

            });
            //builder.addInterceptor(new RetrofitInterceptorCookie());
        }
    }

    private void setCache(boolean setCache, int cacheSize) {
        if (setCache && RxRetrofitConfig.getApp() != null) {
            okHttpClient.cache(new Cache(new File(RxRetrofitConfig.getApp().getCacheDir(), "cache"), cacheSize));
            okHttpClient.addNetworkInterceptor(new RetrofitInterceptorCache());
        }
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
