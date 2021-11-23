package com.example.william.my.module.utils;

import android.widget.Toast;

import com.example.william.my.library.base.BaseApp;

public class T {

    public static void show(String msg) {
        Toast.makeText(BaseApp.getApp(), msg, Toast.LENGTH_SHORT).show();
    }
}
