package com.example.william.my.core.okhttp;

import android.app.Application;

import com.example.william.my.core.okhttp.compat.CompatCache;
import com.example.william.my.core.okhttp.compat.CompatCookieJar;
import com.example.william.my.core.okhttp.compat.CompatHttpsSSL;
import com.example.william.my.core.okhttp.compat.CompatLogging;
import com.example.william.my.core.okhttp.compat.CompatTimeout;

import java.net.Proxy;

import okhttp3.OkHttpClient;

public class OkHttpHelper {

    private static OkHttpHelper instance;

    private static OkHttpClient.Builder okHttpClient;

    public static OkHttpHelper getInstance() {
        if (instance == null) {
            synchronized (OkHttpHelper.class) {
                if (instance == null) {
                    instance = new OkHttpHelper();
                }
            }
        }
        return instance;
    }

    private OkHttpHelper() {
        okHttpClient = new OkHttpClient.Builder();
    }

    private long default_timeout = 60;

    public void setTimeOut(long timeout) {
        default_timeout = timeout;
    }

    private boolean noProxy = false;

    public void setNoProxy() {
        noProxy = true;
    }

    private boolean logShow = true;

    private String logTag = "MyApplication";

    public void setLog(boolean logShow) {
        this.logShow = logShow;
    }

    public void setLog(boolean logShow, String logTag) {
        this.logShow = logShow;
        this.logTag = logTag;
    }

    private boolean cookieJar = true;

    public void setCookieJar(boolean cookieJar) {
        this.cookieJar = cookieJar;
    }

    private boolean ignoreSSL = true;

    public void setIgnoreSSL(boolean ignoreSSL) {
        this.ignoreSSL = ignoreSSL;
    }

    private boolean cache = false;

    private Application app;

    private String cacheDir;

    private long cacheSize;

    public void setCache(Application app, String cacheDir, long cacheSize) {
        cache = true;
        this.app = app;
        this.cacheDir = cacheDir;
        this.cacheSize = cacheSize;
    }

    public OkHttpClient create() {

        //设置超时时间
        CompatTimeout.setTimeOut(okHttpClient, default_timeout);

        //设置连接使用的HTTP代理。该方法优先于proxySelector，默认代理为空，完全禁用代理使用NO_PROXY
        if (noProxy) {
            okHttpClient.proxy(Proxy.NO_PROXY);
        }

        //显示log
        if (logShow) {
            CompatLogging.setLog(okHttpClient, logTag);
        }

        //携带cookie
        if (cookieJar) {
            CompatCookieJar.cookieJar(okHttpClient);
        }

        //忽略https证书
        if (ignoreSSL) {
            CompatHttpsSSL.ignoreSSLForOkHttp(okHttpClient);
            CompatHttpsSSL.ignoreSSLForHttpsURLConnection();
        }

        //设置缓存
        if (cache) {
            CompatCache.cache(app, okHttpClient, cacheDir, cacheSize);
        }

        //if (downloadListener != null) {
        //    builder.addInterceptor(new RetrofitInterceptorProgress(downloadListener));
        //}

        return okHttpClient.build();
    }
}
