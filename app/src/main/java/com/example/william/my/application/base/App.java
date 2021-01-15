package com.example.william.my.application.base;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.library.MyEventBusIndex;
import com.example.william.my.library.base.BaseApp;
import com.example.william.my.module.BuildConfig;
import com.example.william.my.module.MyModuleEventBusIndex;
import com.example.william.my.open.MyModuleOpenEventBusIndex;

import org.greenrobot.eventbus.EventBus;

public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        initARouter();

        initEventBus();
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();//打印日志
            ARouter.openDebug();//开启调试模式
        }
        ARouter.init(getApp());//初始化
    }

    private void initEventBus() {
        EventBus.builder()
                .addIndex(new MyEventBusIndex())
                .addIndex(new MyModuleEventBusIndex())
                .addIndex(new MyModuleOpenEventBusIndex())
                .build();
    }
}
