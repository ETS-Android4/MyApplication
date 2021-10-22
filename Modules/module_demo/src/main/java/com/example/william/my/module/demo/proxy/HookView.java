package com.example.william.my.module.demo.proxy;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 2. 创建反射管理类，用于保存hook到的OnClickListener对象
 */
public class HookView {

    public Method mHookMethod;
    public Field mHookField;

    @SuppressLint("PrivateApi,DiscouragedPrivateApi")
    public HookView(View view) {
        try {
            Class<?> viewClass = Class.forName("android.view.View");
            mHookMethod = viewClass.getDeclaredMethod("getListenerInfo");
            mHookMethod.setAccessible(true);
            Class<?> listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
            mHookField = listenerInfoClass.getDeclaredField("mOnClickListener");
            mHookField.setAccessible(true);
        } catch (Exception e) {
            Log.e("TAG", "hook clickListener failed!", e);
        }
    }
}