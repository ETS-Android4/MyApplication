package com.example.william.my.module;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.event.DefaultSmartEventBus;
import com.example.william.my.module.event.MessageEvent;
import com.example.william.my.module.event.SmartMessageEvent;
import com.example.william.my.module.router.ARouterPath;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Plugins:
 * GsonForMartPlus
 * Google Library Version Querier
 * Alibaba Java Coding Guidelines
 */
@Route(path = ARouterPath.Module)
public class ModuleActivity extends BaseListActivity {

    @Autowired(name = "param_key")
    public String mModuleParam;

    @Override
    protected void initData() {
        super.initData();
        mMap.put("AndroidUtilActivity", ARouterPath.Util.Util);

        mMap.put("NetWorkActivity", ARouterPath.NetWork.NetWork);
        mMap.put("CustomViewActivity", ARouterPath.CustomView.CustomView);

        mMap.put("JetPackActivity", ARouterPath.JetPack.JetPack);
        mMap.put("OpenSourceActivity", ARouterPath.OpenSource.OpenSource);

        mMap.put("WidgetActivity", ARouterPath.Widget.Widget);
        mMap.put("DemoActivity", ARouterPath.Demo.Demo);

        //Kotlin
        mMap.put("KotlinActivity", ARouterPath.Kotlin.Kotlin);
        mMap.put("SampleActivity", ARouterPath.Sample.Sample);

        // 网易云信直播间
        mMap.put("NeRtcActivity", ARouterPath.NeRtc.Audio);

        //Flutter
        //mMap.put("Flutter", "Flutter");
        //mMap.put("FlutterActivity", ARouterPath.Flutter.Flutter);

        LiveEventBus
                .get("some_key", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        Toast.makeText(ModuleActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });

        DefaultSmartEventBus
                .event1()
                .observe(this, new Observer<SmartMessageEvent>() {
                    @Override
                    public void onChanged(SmartMessageEvent event) {
                        Toast.makeText(ModuleActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.d(TAG, "addIdleHandler: queueIdle " + Thread.currentThread().getName());
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(ModuleActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
    }
}