package com.example.william.my.module.router.navigation;

import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;

/**
 * 登录拦截器
 */
public class LoginNavigationImpl implements NavigationCallback {

    private static final String TAG = "LoginNavigation";

    @Override
    public void onFound(Postcard postcard) {
        Log.i(TAG, "找到了:" + postcard.getPath());
    }

    @Override
    public void onLost(Postcard postcard) {
        Log.i(TAG, "找不到了:" + postcard.getPath());
    }

    @Override
    public void onArrival(Postcard postcard) {
        Log.i(TAG, "跳转完了:" + postcard.getPath());
    }

    @Override
    public void onInterrupt(Postcard postcard) {
        Log.i(TAG, "被拦截了:" + postcard.getPath());
    }
}