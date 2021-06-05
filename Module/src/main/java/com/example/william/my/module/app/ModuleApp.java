package com.example.william.my.module.app;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.library.interfaces.IComponentApplication;
import com.example.william.my.module.BuildConfig;
import com.example.william.my.module.utils.Crash;

public class ModuleApp implements IComponentApplication {

    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void init(Application application) {
        initCrash(application);
        initARouter(application);
    }

    @Override
    public void initAsync(Application application) {

    }

    private void initCrash(Application application) {
        Crash.init(new Crash.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                Log.e(TAG, crashInfo);
                Toast.makeText(application, crashInfo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initARouter(Application application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();//打印日志
            ARouter.openDebug();//开启调试模式
        }
        ARouter.init(application);//初始化
    }
}
