package com.example.william.my.module.util.activity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.william.my.module.activity.BaseResponseActivity;

public class AppUtilsActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        // 获取所有已安装 App 信息
        LogUtils.e(TAG, AppUtils.getAppsInfo().toString());
        // 判断 App 是否安装
        //LogUtils.e(TAG, AppUtils.isAppInstalled("com.android.phone"));
        // 安装 App（支持 8.0）
        AppUtils.installApp("");
        // 卸载 App
        AppUtils.uninstallApp("");
    }
}