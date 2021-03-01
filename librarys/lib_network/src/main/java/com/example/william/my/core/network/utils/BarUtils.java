package com.example.william.my.core.network.utils;

import android.content.res.Resources;

public class BarUtils {

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
