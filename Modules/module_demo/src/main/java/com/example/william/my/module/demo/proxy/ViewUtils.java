package com.example.william.my.module.demo.proxy;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

    public static String getAbsolutePath(View view) {
        if (view == null) {
            return "";
        }
        if (view.getParent() == null) {
            return "rootView";
        }

        View temp = view;
        StringBuilder path = new StringBuilder();
        while (temp.getParent() != null && temp.getParent() instanceof View) {
            int index = 0;
            try {
                index = indexOfChild((ViewGroup) temp.getParent(), temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            path.insert(0, temp.getClass().getSimpleName() + '[' + index + "]/");
            temp = (View) temp.getParent();
        }
        return path.substring(0, path.length() - 1);
    }

    private static int indexOfChild(ViewGroup parent, View child) {
        if (parent == null) {
            return 0;
        }
        int count = parent.getChildCount();
        int j = 0;
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (child.getClass().isInstance(view)) {
                if (view == child) {
                    return j;
                }
                j++;
            }
        }
        return -1;
    }
}
