
package com.example.william.my.module.bugly.app;

import android.app.Application;

import com.tencent.bugly.Bugly;

public class BuglyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "fd7808109f", false);
    }
}
