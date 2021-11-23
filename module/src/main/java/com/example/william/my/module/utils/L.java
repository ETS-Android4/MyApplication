package com.example.william.my.module.utils;

import android.util.Log;

public class L {

    //规定每段显示的长度
    private static final int MAX_LENGTH = 4000;

    public static void e(String TAG, String msg) {
        String temp;
        int index = 0;
        while (index < msg.length()) {
            // java的字符不允许指定超过总的长度end
            if (msg.length() <= index + MAX_LENGTH) {
                temp = msg.substring(index);
            } else {
                temp = msg.substring(index, index + MAX_LENGTH);
            }
            index += MAX_LENGTH;
            Log.e(TAG, temp.trim());
        }
    }

    public static void i(String TAG, String msg) {
        String temp;
        int index = 0;
        while (index < msg.length()) {
            // java的字符不允许指定超过总的长度end
            if (msg.length() <= index + MAX_LENGTH) {
                temp = msg.substring(index);
            } else {
                temp = msg.substring(index, index + MAX_LENGTH);
            }
            index += MAX_LENGTH;
            Log.i(TAG, temp.trim());
        }
    }
}
