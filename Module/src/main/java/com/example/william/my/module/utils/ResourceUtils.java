package com.example.william.my.module.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {

    public static String getAssets(Context context, String assetsFilePath) {
        String result = "";
        try {
            InputStream mAssets = context.getAssets().open(assetsFilePath);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = mAssets.read(b, 0, 1024)) != -1) {
                os.write(b, 0, len);
            }
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }
}
