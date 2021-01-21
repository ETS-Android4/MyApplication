package com.example.william.my.module.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.william.my.library.base.BaseApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UriUtils {

    public static Uri save(InputStream is, String fileName) {
        ContentValues contentValues = new ContentValues();
        // 设置文件名
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        // 指定次级目录
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + BaseApp.getApp().getPackageName());
        Uri uri = BaseApp.getApp().getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        if (uri == null) {
            return null;
        }
        OutputStream os = null;
        try {
            os = BaseApp.getApp().getContentResolver().openOutputStream(uri);
            byte[] data = new byte[1024];
            for (int len; (len = is.read(data)) != -1; ) {
                os.write(data, 0, len);
            }
            contentValues.clear();
            BaseApp.getApp().getContentResolver().update(uri, contentValues, null, null);
            return uri;
        } catch (Exception e) {
            BaseApp.getApp().getContentResolver().delete(uri, null, null);
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void isExist() {
        Cursor cursor = BaseApp.getApp().getContentResolver().query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.e("UriUtils", "fileName");
        }
        cursor.close();
    }
}
