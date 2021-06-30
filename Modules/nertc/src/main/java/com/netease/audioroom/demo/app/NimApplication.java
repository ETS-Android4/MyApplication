package com.netease.audioroom.demo.app;


import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.util.IconFontUtil;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.model.NESDKConfig;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.netease.yunxin.android.lib.network.common.NetworkClient;
import com.netease.yunxin.kit.alog.ALog;

import java.util.Map;

/**
 * 注意：每个进程都会创建自己的Application 然后调用onCreate()方法，
 * 如果用户有自己的逻辑需要写在Application#onCreate()（还有Application的其他方法）中，一定要注意判断进程，不能把业务逻辑写在core进程，
 * 理论上，core进程的Application#onCreate()（还有Application的其他方法）只能做与im sdk 相关的工作
 */
public class NimApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkClient.getInstance().configBaseUrl(BuildConfig.SERVER_BASE_URL)
                .appKey(BuildConfig.NIM_APP_KEY)
                .configDebuggable(true);
        // 播放器初始化，用于 CDN 拉流
        NESDKConfig config = new NESDKConfig();
        config.dataUploadListener = new NELivePlayer.OnDataUploadListener() {
            @Override
            public boolean onDataUpload(String url, String data) {
                ALog.e("Player===>", "stream url is " + url + ", detail data is " + data);
                return true;
            }

            @Override
            public boolean onDocumentUpload(String url, Map<String, String> params, Map<String, String> filepaths) {
                return false;
            }
        };
        NELivePlayer.init(getApplicationContext(), config);

        DemoCache.init(this);
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将进行自动登录）。不能对初始化语句添加进程判断逻辑。
        NIMClient.init(this, null, getOptions());
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            IconFontUtil.getInstance().init(this);
            initLog();
        }
    }

    private void initLog() {
        ALog.init(this, BuildConfig.DEBUG ? ALog.LEVEL_ALL : ALog.LEVEL_INFO);

//        BasicInfo basicInfo = new BasicInfo.Builder()
//                .name(getString(R.string.app_name),true)
//                .version("v"+BuildConfig.VERSION_NAME)
//                .baseUrl(BuildConfig.SERVER_BASE_URL)
//                .deviceId(this)
//                .packageName(this)
//                .imVersion(BuildConfig.IM_VERSION)
//                .nertcVersion(BuildConfig.NERTC_VERSION)
//                .build();
//        ALog.logFirst(basicInfo);
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();
        options.appKey = BuildConfig.NIM_APP_KEY;
        return options;
    }

}
