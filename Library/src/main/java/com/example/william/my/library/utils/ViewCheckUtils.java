package com.example.william.my.library.utils;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 判断View是否可见
 */
public class ViewCheckUtils {

    public static Boolean checkIsVisible(Activity activity, View view) {
        // 如果已经加载了，判断view是否显示
        int screenWidth = getScreenMetrics(activity).x;
        int screenHeight = getScreenMetrics(activity).y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return view.getLocalVisibleRect(rect);
    }

    public static Point getScreenMetrics(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int w_screen = metrics.widthPixels;
        int h_screen = metrics.heightPixels;
        return new Point(w_screen, h_screen);
    }
}
