package com.example.william.my.module.jiguang.app;

import com.example.william.my.library.BuildConfig;
import com.example.william.my.library.base.BaseApp;

import cn.jiguang.verifysdk.api.JVerificationInterface;

public class JApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        JVerificationInterface.init(this);
        if (BuildConfig.DEBUG) {
            JVerificationInterface.setDebugMode(true);
        }
    }
}
