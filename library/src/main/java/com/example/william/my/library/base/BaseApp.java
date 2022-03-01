package com.example.william.my.library.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.william.my.library.interfaces.IComponentApplication;
import com.example.william.my.library.utils.ActivityManagerUtil;

import java.util.concurrent.Executors;

/**
 * Application
 */
public class BaseApp extends Application {
    //public class BaseApp extends MultiDexApplication {

    protected final String TAG = this.getClass().getSimpleName();

    private static BaseApp instance;

    /**
     * application单例
     */
    public static BaseApp getApp() {
        if (instance == null) {
            instance = new BaseApp();
        }
        return instance;
    }

    private static final String[] modulesList = {
            "com.example.william.my.module.app.ModuleApp",
            "com.example.william.my.module.network.app.NetWorkApp",
            "com.example.william.my.module.sample.app.SampleApp",
            "com.example.william.my.module.opensource.app.OpenSourceApp",
            "com.example.william.my.module.demo.app.DemoApp"
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化MultiDex
        //MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        modulesApplicationInit();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //设置线程的优先级，不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //Module类的APP初始化异步
                modulesApplicationInitAsync();
            }
        });

        //registerActivityLifecycleCallbacks();
    }

    private void modulesApplicationInit() {
        for (String moduleImpl : modulesList) {
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof IComponentApplication) {
                    ((IComponentApplication) obj).init(this);
                }
            } catch (NullPointerException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void modulesApplicationInitAsync() {
        for (String moduleImpl : modulesList) {
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof IComponentApplication) {
                    ((IComponentApplication) obj).initAsync(this);
                }
            } catch (NullPointerException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerActivityLifecycleCallbacks() {
        //监听activity生命周期
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                ActivityManagerUtil.getInstance().addActivity(activity);
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
                ActivityManagerUtil.getInstance().removeActivity(activity);
            }
        });
    }
}
