package com.example.william.my.library.interfaces;

import android.app.Application;

/**
 * 合并多个Application
 */
public interface IComponentApplication {

    void init(Application application);

    void initAsync(Application application);
}
