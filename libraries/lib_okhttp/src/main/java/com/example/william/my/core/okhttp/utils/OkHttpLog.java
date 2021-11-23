package com.example.william.my.core.okhttp.utils;

import android.util.Log;

public class OkHttpLog {

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }
}
