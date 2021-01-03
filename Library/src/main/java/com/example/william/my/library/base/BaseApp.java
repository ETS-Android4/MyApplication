package com.example.william.my.library.base;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.example.william.my.library.interfaces.IComponentApplication;

/**
 * Application
 */
public class BaseApp extends Application {
    //public class BaseApp extends MultiDexApplication {

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
            "com.example.william.my.module.base.ModuleApp",
            "com.example.william.my.open.base.OpenSourceApp"
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程的优先级，不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //Module类的APP初始化异步
                modulesApplicationInitAsync();
            }
        }).start();
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
}
