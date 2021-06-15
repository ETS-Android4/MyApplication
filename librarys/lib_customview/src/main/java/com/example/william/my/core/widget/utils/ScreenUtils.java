package com.example.william.my.core.widget.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;

public class ScreenUtils {

    public static int getScreenWidth(Context context) {
        Point point = new Point();
        context.getDisplay().getRealSize(point);
        return point.x;
    }

    public static int getScreenHeight(Context context) {
        Point point = new Point();
        context.getDisplay().getRealSize(point);
        return point.y;
    }

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
