package com.netease.audioroom.demo.app;

import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.ChatHelper;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.yunxin.android.lib.network.common.NetworkClient;

public class NimApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkClient.getInstance().configBaseUrl(BuildConfig.SERVER_BASE_URL)
                .appKey(BuildConfig.NIM_APP_KEY)
                .configDebuggable(true);

        ChatHelper.initPlayer(getApplicationContext());//播放器初始化

        DemoCache.init(this);//用户数据初始化

        ChatHelper.initIM(this, null);//IM 初始化
    }
}
