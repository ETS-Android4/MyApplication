package com.example.william.my.core.okhttp.compat;

import android.content.Context;
import android.os.Environment;

import com.example.william.my.core.okhttp.interceptor.InterceptorCache;
import com.example.william.my.core.okhttp.utils.OkHttpLog;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class CompatCache {

    public static void cache(Context context, OkHttpClient.Builder builder, String cacheDir, long cacheSize) {
        if (context != null) {
            builder.cache(new Cache(new File(getCacheDir(context), cacheDir), cacheSize));
            builder.addNetworkInterceptor(new InterceptorCache(context));
        } else {
            OkHttpLog.e("CompatCache", "context == null. 缓存未启用.");
        }
    }

    public static String getCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
