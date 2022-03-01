package com.example.william.my.module.demo;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.william.my.library.utils.FileSDCardUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextUtils {

    public static void writeFilesDir(Context context) {
        File file = new File(context.getFilesDir(), "filename.txt");
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write("json数据".getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * /storage/emulated/0/Android/data/包名/files
     *
     * @param context
     */
    public static void writeExternalFilesDir(Context context) {
        File file = new File(context.getExternalFilesDir(""), "filename.txt");
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write("json数据".getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * /storage/emulated/0/Android/data/包名/files
     *
     * @param context
     */
    public static void writeExternalFilesDirByUtils(Context context) {
        File file = new File(FileSDCardUtil.getFilesPath(context), "filename.txt");
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write("json数据".getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void get(Context context) {
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
