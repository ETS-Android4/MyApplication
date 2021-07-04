package com.netease.audioroom.demo.app;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.os.Bundle;

import com.netease.audioroom.demo.base.BaseActivityManager;
import com.netease.audioroom.demo.receiver.NetworkConnectChangedReceiver;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkUtils;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.audioroom.demo.widget.loadsir.callback.EmptyChatRoomListCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.EmptyMuteRoomListCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.FailureCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.NetErrCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.TimeoutCallback;
import com.netease.audioroom.demo.widget.loadsir.core.LoadSir;
import com.netease.yunxin.kit.alog.ALog;

public class BaseApplication extends Application {

    private final NetworkConnectChangedReceiver networkConnectChangedReceiver = new NetworkConnectChangedReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
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

        registerActivity();

        registerReceiver();
    }

    /**
     * 监听activity生命周期
     */
    private void registerActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                BaseActivityManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                BaseActivityManager.getInstance().removeActivity(activity);
            }
        });
    }

    /**
     * 网络变化观察者
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGE");
        filter.addAction("android.net.conn.STATE_CHANGE");
        registerReceiver(networkConnectChangedReceiver, filter);
    }
}
