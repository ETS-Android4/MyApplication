package com.example.william.my.library.interfaces;

import android.app.Application;

/**
 * 合并多个Application
 */
public interface IComponentApplication {

    public void init(Application application);

    public void initAsync(Application application);
}
