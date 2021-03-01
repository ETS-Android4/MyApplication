package com.example.william.my.module.jetpack.app;

import android.app.Application;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.library.interfaces.IComponentApplication;

public class JetPackApp extends IComponentApplication {

    @Override
    public void init(Application application) {
        super.init(application);
        RxRetrofitConfig.init(application);
    }
}
