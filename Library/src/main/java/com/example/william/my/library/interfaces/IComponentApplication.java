package com.example.william.my.library.interfaces;

import android.app.Application;
import android.util.Log;

/**
 * 合并多个Application
 */
public abstract class IComponentApplication {

    public void init(Application application) {
        Log.e("TAG", getClass().getSimpleName() + " : init()");
    }

    public void initAsync(Application application) {
        Log.e("TAG", getClass().getSimpleName() + " : init()");
    }
}
