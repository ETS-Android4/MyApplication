package com.example.william.my.module.demo.proxy;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 1. 创建监听代理管理类，用于统一管理OnClickListener对象的调用即实现
 */
public class ProxyManager {

    public static void sendLog(View view) {

    }

    public static class ProxyListener implements View.OnClickListener {

        View.OnClickListener mOriginalListener;

        public ProxyListener(View.OnClickListener originalListener) {
            mOriginalListener = originalListener;
        }

        @Override
        public void onClick(View v) {
            // TODO: send log
            sendLog(v);

            if (mOriginalListener != null) {
                mOriginalListener.onClick(v);
            }
        }
    }

    public void hookViews(View view) {
        try {
            if (view.getVisibility() == View.VISIBLE) {
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    int count = group.getChildCount();
                    for (int i = 0; i < count; i++) {
                        View child = group.getChildAt(i);
                        hookViews(child);
                    }
                } else {
                    if (view.isClickable()) {
                        HookView hookView = new HookView(view);
                        Object listenerInfo = hookView.mHookMethod.invoke(view);
                        Object originalListener = hookView.mHookField.get(listenerInfo);
                        hookView.mHookField.set(listenerInfo, new ProxyManager.ProxyListener((View.OnClickListener) originalListener));
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "hook clickListener failed!", e);
        }
    }
}