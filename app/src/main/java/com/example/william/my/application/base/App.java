package com.example.william.my.application.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.example.william.my.library.base.BaseApp;
import com.example.william.my.module.BuildConfig;
import com.example.william.my.module.MyModuleEventBusIndex;

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
        //ARouter初始化
        ARouter.init(getApp());
    }

    private void initEventBus() {
        EventBus.builder()
                .addIndex(new MyModuleEventBusIndex())
                //.addIndex(new MyModuleOpenEventBusIndex())
                .build();
    }

    private void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                Log.e(TAG, activity.getClass().getSimpleName());
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
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}
