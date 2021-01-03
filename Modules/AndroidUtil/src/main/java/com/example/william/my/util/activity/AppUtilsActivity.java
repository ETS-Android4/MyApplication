package com.example.william.my.util.activity;

import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.example.william.my.module.activity.ResponseActivity;

public class AppUtilsActivity extends ResponseActivity {

    @Override
    public void initView() {
        super.initView();

        // 获取所有已安装 App 信息
        Log.e(TAG, AppUtils.getAppsInfo().toString());
        // 判断 App 是否安装
        //Log.e(TAG, AppUtils.isAppInstalled("com.android.phone"));
        // 安装 App（支持 8.0）
        AppUtils.installApp("");
        // 卸载 App
        AppUtils.uninstallApp("");
    }
}