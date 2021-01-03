package com.example.william.my.util.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheMemoryStaticUtils;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.util.R;

import java.io.File;
import java.util.Objects;

/**
 * 内部存储：
 * 通过Context.getFilesDir()方法可以获取到 /data/user/0/你的应用包名/files
 * 通过Context.getCacheDir()方法可以获取到 /data/user/0/你的应用包名/cache
 * 外部存储：
 * 通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用包名/files/目录，一般放一些长时间保存的数据
 * 通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
 * 两个目录分别对应 设置->应用->应用详情里面的”清除数据“与”清除缓存“选项
 * <p>
 * Storage Access Framework存储访问框架 (SAF)
 */
@Route(path = ARouterPath.Util.Util_Cache)
public class CacheActivity extends ResponseActivity {

    @Override
    public void initView() {
        super.initView();

        mResponse.setText("");
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher);

        // 内存缓存
        CacheMemoryStaticUtils.put("ic_launcher", drawable);
        mResponse.setBackground((Drawable) CacheMemoryStaticUtils.get("ic_launcher"));

        // 磁盘缓存
        CacheDiskStaticUtils.put("ic_launcher", drawable);
        mResponse.setBackground(CacheDiskStaticUtils.getDrawable("ic_launcher"));


        //deprecated
        //Log.e(TAG, Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getPath());
        //Log.e(TAG, Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath());

        //FileIOUtils.writeFileFromString(getDiskCacheDir(this) + TAG + ".txt", "content");
    }

    public String getDiskCacheDir(Context context) {
        File cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Objects.requireNonNull(context.getExternalCacheDir());
        } else {
            cachePath = Objects.requireNonNull(context.getCacheDir());
        }
        return cachePath.getPath();
    }
}