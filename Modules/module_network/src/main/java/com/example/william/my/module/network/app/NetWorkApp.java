package com.example.william.my.module.network.app;

import android.app.Application;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.library.interfaces.IComponentApplication;

public class NetWorkApp implements IComponentApplication {

    @Override
    public void init(Application application) {
        RxRetrofitConfig.init(application);
    }

    @Override
    public void initAsync(Application application) {

    }
}
