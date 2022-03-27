package com.example.william.my.library.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    public static String getFilePath(Context context) {
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
    public static String getCachePath(Context context) {
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

    public static void writeCacheDir(Context context, String name, String str) {
        File file = new File(FileSDCardUtil.getCachePath(context), name);
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileDir(Context context, String name, String str) {
        File file = new File(FileSDCardUtil.getFilePath(context), name);
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getUri(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//查询数据库
                null,//返回ID的List
                null,//查询条件
                null,//查询条件参数，替换第三个参数中的?
                null);//排序
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                Log.e("TAG", "image uri is" + uri);
            }
            cursor.close();
        }
    }
}


