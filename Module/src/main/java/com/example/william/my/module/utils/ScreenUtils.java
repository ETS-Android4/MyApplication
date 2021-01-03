package com.example.william.my.module.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.example.william.my.module.app.ModuleApp;

public class ScreenUtils {

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) ModuleApp.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) ModuleApp.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }
}
