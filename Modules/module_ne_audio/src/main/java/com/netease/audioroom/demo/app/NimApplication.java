package com.netease.audioroom.demo.app;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.william.my.library.interfaces.IComponentApplication;
import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.ChatHelper;
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

public class NimApplication extends Application implements IComponentApplication {

    private final NetworkConnectChangedReceiver networkConnectChangedReceiver = new NetworkConnectChangedReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        initNim();
    }

    private void initNim(){
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

        NetworkClient.getInstance().configBaseUrl(BuildConfig.SERVER_BASE_URL)
                .appKey(BuildConfig.NIM_APP_KEY)
                .configDebuggable(true);

        DemoCache.init(this);//用户数据初始化

        ChatHelper.initPlayer(getApplicationContext());//播放器初始化

        ChatHelper.initIM(this, null);//IM 初始化
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

    @Override
    public void init(Application application) {
        Log.e("TAG", "init");
    }

    @Override
    public void initAsync(Application application) {

    }
}
