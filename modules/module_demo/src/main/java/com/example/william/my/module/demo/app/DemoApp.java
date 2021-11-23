package com.example.william.my.module.demo.app;

import android.app.Application;

import com.example.william.my.library.interfaces.IComponentApplication;
import com.example.william.my.module.demo.proxy.HookTrack;

public class DemoApp implements IComponentApplication {

    @Override
    public void init(Application application) {
        HookTrack.INSTANCE.init(application);
    }

    @Override
    public void initAsync(Application application) {

    }
}
