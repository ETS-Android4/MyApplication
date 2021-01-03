package com.example.william.my.module.app;

import android.app.Application;
import android.util.Log;

import com.example.william.my.library.base.BaseApp;
import com.example.william.my.library.interfaces.IComponentApplication;
import com.example.william.my.module.utils.Crash;

public class ModuleApp extends BaseApp implements IComponentApplication {

    public final String TAG = this.getClass().getSimpleName();

    @Override
    public void init(Application application) {
        Crash.init(new Crash.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                Log.e(TAG, crashInfo);
            }
        });
    }

    @Override
    public void initAsync(Application application) {

    }
}
