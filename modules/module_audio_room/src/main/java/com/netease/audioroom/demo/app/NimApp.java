package com.netease.audioroom.demo.app;

import android.app.Application;

import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.ChatHelper;
import com.netease.audioroom.demo.base.BaseActivityManager;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.yunxin.android.lib.network.common.NetworkClient;

public class NimApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initNim(this);
    }

    private void initNim(Application application) {
        BaseActivityManager.getInstance().register(application);

        NetworkClient.getInstance().configBaseUrl(BuildConfig.SERVER_BASE_URL)
                .appKey(BuildConfig.NIM_APP_KEY)
                .configDebuggable(true);

        DemoCache.init(application);//用户数据初始化

        ChatHelper.initPlayer(getApplicationContext());//播放器初始化

        ChatHelper.initIM(application, null);//IM 初始化
    }
}
