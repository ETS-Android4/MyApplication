package com.example.william.my.application.base;

import com.alibaba.android.arouter.launcher.ARouter;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.example.william.my.library.MyEventBusIndex;
import com.example.william.my.library.base.BaseApp;
import com.example.william.my.module.BuildConfig;
import com.example.william.my.module.MyModuleEventBusIndex;
import com.example.william.my.module.open.MyModuleOpenEventBusIndex;

import org.greenrobot.eventbus.EventBus;

public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        initDoKit();

        initARouter();

        initEventBus();
    }

    /**
     * http://xingyun.xiaojukeji.com/docs/dokit/#/intro
     */
    private void initDoKit() {
        DoraemonKit.install(this, "e7fc111f414979edcc27b6251e72ab7d");
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
