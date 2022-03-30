package com.example.william.my.module.libraries.app;

import android.app.Application;

import com.example.william.my.core.eventbus.flow.FlowEventBus;
import com.example.william.my.library.interfaces.IComponentApplication;

public class LibrariesApp implements IComponentApplication {

    @Override
    public void init(Application application) {
        FlowEventBus.INSTANCE.init(application);
    }

    @Override
    public void initAsync(Application application) {

    }
}
