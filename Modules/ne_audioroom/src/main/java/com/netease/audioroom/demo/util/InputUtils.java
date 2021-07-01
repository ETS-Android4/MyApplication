package com.netease.audioroom.demo.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 软键盘工具类
 */
public final class InputUtils {
    /**
     * 展示软键盘
     *
     * @param inputView 输入框
     */
    public static void showSoftInput(Context context, View inputView) {
        if (inputView == null) {
            return;
        }
        InputMethodManager service = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (service == null) {
            return;
        }
        inputView.setVisibility(VISIBLE);
        inputView.requestFocus();
        service.showSoftInput(inputView, 0);
    }

    /**
     * 隐藏软键盘
     *
     * @param inputView 输入框
     */
    public static void hideSoftInput(Context context, View inputView) {
        if (inputView == null) {
            return;
        }
        InputMethodManager service = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (service == null) {
            return;
        }
        inputView.setVisibility(GONE);
        service.hideSoftInputFromWindow(inputView.getWindowToken(), 0);
    }
}
