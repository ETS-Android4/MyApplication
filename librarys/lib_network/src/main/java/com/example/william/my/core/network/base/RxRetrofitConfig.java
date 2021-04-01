package com.example.william.my.core.network.base;

import android.app.Application;

public class RxRetrofitConfig {

    private static Application instance;

    public static final long connectTimeout = 60;
    public static final long writeTimeout = 60;
    public static final long readTimeout = 60;
    public static final long callTimeout = 60;

    public static final boolean showLog = true;

    //是否设置缓存
    public static final boolean setCache = false;
    //设置缓存大小
    public static final int cacheSize = 100 * 1024 * 1024;//100M

    public static void init(Application context) {
        instance = context;
    }

    public static Application getApp() {
        return instance;
    }
}
