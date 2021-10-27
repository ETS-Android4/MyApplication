package com.example.william.my.library.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 内部存储：
 * 通过Context.getFilesDir()方法可以获取到 /data/user/0/你的应用包名/files
 * 通过Context.getCacheDir()方法可以获取到 /data/user/0/你的应用包名/cache
 * 外部存储：
 * 通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用包名/files/目录，一般放一些长时间保存的数据
 * 通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
 * 两个目录分别对应 设置->应用->应用详情里面的”清除数据“与”清除缓存“选项
 */
public class FileSDCardUtil {

    /**
     * @return /storage/emulated/0/Android/data/包名/files
     */
    public String getFilesPath(Context context) {
        String filePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            filePath = context.getExternalFilesDir(null).getPath();
        } else {
            //外部存储不可用
            filePath = context.getFilesDir().getPath();
        }
        return filePath;
    }

    /**
     * @return /storage/emulated/0/Android/data/包名/cache
     */
    public String getCachePath(Context context) {
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

    @SuppressWarnings("deprecation")
    public static String getSdCardPublic() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File directoryDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            return directoryDownloads.getAbsolutePath();
        }
        return "";
    }

    @SuppressWarnings("deprecation")
    public static String getSdCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator);
            return directory.getAbsolutePath();
        }
        return "";
    }
}


