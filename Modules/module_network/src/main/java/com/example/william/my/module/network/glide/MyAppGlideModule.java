package com.example.william.my.module.network.glide;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 生成GlideApp
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        //设置缓存大小为100MB
        int memoryCacheSizeBytes = 1024 * 1024 * 100; // 100MB
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        //根据SD卡是否可用选择是在内部缓存还是SD卡缓存
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "MyApplication", memoryCacheSizeBytes));
        } else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "MyApplication", memoryCacheSizeBytes));
        }
    }
}