package com.netease.audioroom.demo.app;

import android.app.Application;
import android.content.IntentFilter;

import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.act.ChatHelper;
import com.netease.audioroom.demo.base.BaseActivityManager;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.receiver.NetworkConnectChangedReceiver;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkUtils;
import com.netease.audioroom.demo.widget.loadsir.callback.EmptyChatRoomListCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.EmptyMuteRoomListCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.FailureCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.NetErrCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.TimeoutCallback;
import com.netease.audioroom.demo.widget.loadsir.core.LoadSir;
import com.netease.yunxin.android.lib.network.common.NetworkClient;

public class NimApp extends Application {

    private final NetworkConnectChangedReceiver networkConnectChangedReceiver = new NetworkConnectChangedReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        initNim(this);
    }

    private void initNim(Application application) {
        if (NetworkUtils.isNetworkConnected(application)) {
            Network network = Network.getInstance();
            network.setConnected(true);
        }
        //同一页面初始化
        LoadSir.beginBuilder()
                .addCallback(new FailureCallback())
                .addCallback(new EmptyChatRoomListCallback())
                .addCallback(new NetErrCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new EmptyChatRoomListCallback())
                .addCallback(new EmptyMuteRoomListCallback())
                .setDefaultCallback(FailureCallback.class)
                .commit();

        BaseActivityManager.getInstance().register(application);

        registerReceiver(application);

        NetworkClient.getInstance().configBaseUrl(BuildConfig.SERVER_BASE_URL)
                .appKey(BuildConfig.NIM_APP_KEY)
                .configDebuggable(true);

        DemoCache.init(application);//用户数据初始化

        ChatHelper.initPlayer(getApplicationContext());//播放器初始化

        ChatHelper.initIM(application, null);//IM 初始化
    }


    /**
     * 网络变化观察者
     */
    private void registerReceiver(Application application) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGE");
        filter.addAction("android.net.conn.STATE_CHANGE");
        application.registerReceiver(networkConnectChangedReceiver, filter);
    }
}
