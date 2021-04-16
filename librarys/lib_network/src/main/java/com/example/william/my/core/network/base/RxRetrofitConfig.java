package com.example.william.my.core.network.base;

import android.app.Application;

public class RxRetrofitConfig {

    private static Application instance;

    public static void init(Application context) {
        instance = context;
    }

    public static Application getApp() {
        return instance;
    }

    public static final long connectTimeout = 60;
    public static final long writeTimeout = 60;
    public static final long readTimeout = 60;
    public static final long callTimeout = 60;

    //是否显示Log
    public static final boolean showLogging = true;
    //设置Log Tag
    public static final String loggingTag = "MyApplication";

    //是否设置缓存
    public static final boolean setCache = true;
    //设置缓存名
    public static final String cacheDir = "cache";
    //设置缓存大小
    public static final long cacheSize = 10 * 1024 * 1024;//10M

}
