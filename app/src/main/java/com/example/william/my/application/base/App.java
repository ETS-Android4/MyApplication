package com.example.william.my.application.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.example.william.my.library.MyEventBusIndex;
import com.example.william.my.library.base.BaseApp;
import com.example.william.my.module.BuildConfig;
import com.example.william.my.module.MyModuleEventBusIndex;
import com.example.william.my.module.open.MyModuleOpenEventBusIndex;

import org.greenrobot.eventbus.EventBus;

/**
 * gradlew :app:dependencies 查询app依赖
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        initDoKit();

        initARouter();

        initEventBus();

        initActivityLifecycleCallbacks();
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

    private void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}
