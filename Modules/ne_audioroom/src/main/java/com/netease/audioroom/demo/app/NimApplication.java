package com.netease.audioroom.demo.app;


import com.netease.audioroom.demo.ChatHelper;
import com.netease.yunxin.kit.alog.ALog;
import com.netease.yunxin.kit.alog.BasicInfo;
import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.util.IconFontUtil;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.model.NESDKConfig;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.netease.yunxin.android.lib.network.common.NetworkClient;

import java.util.Map;

public class NimApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkClient.getInstance().configBaseUrl(BuildConfig.SERVER_BASE_URL)
                .appKey(BuildConfig.NIM_APP_KEY)
                .configDebuggable(true);

        ChatHelper.initPlayer(getApplicationContext());//播放器初始化

        DemoCache.init(this);//用户数据初始化

        ChatHelper.initIM(this, null);
    }

    private void initLog() {
        ALog.init(this, BuildConfig.DEBUG ? ALog.LEVEL_ALL : ALog.LEVEL_INFO);
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();
        options.appKey = BuildConfig.NIM_APP_KEY;
        return options;
    }

}
