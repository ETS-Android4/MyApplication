package com.example.william.my.module;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseRecyclerActivity;
import com.example.william.my.module.event.DefaultSmartEventBus;
import com.example.william.my.module.event.MessageEvent;
import com.example.william.my.module.event.SmartMessageEvent;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.item.RouterItem;
import com.example.william.my.module.utils.L;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Plugins:
 * GsonForMartPlus
 * Google library Version Querier
 * Alibaba Java Coding Guidelines
 */
@Route(path = ARouterPath.Module)
public class ModuleActivity extends BaseRecyclerActivity {

    @Autowired(name = "param_key")
    public String mModuleParam;

    protected ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        //
        routerItems.add(new RouterItem("AndroidUtilActivity", ARouterPath.Util.Util));
        routerItems.add(new RouterItem("OpenSourceActivity", ARouterPath.OpenSource.OpenSource));
        //
        routerItems.add(new RouterItem(" ", " "));
        routerItems.add(new RouterItem("LibActivity", ARouterPath.Lib.Lib));
        routerItems.add(new RouterItem("DemoActivity", ARouterPath.Demo.Demo));
        //
        routerItems.add(new RouterItem(" ", " "));
        routerItems.add(new RouterItem("NetWorkActivity", ARouterPath.NetWork.NetWork));
        routerItems.add(new RouterItem("SampleActivity", ARouterPath.Sample.Sample));
        // 网易云信直播间
        //routerItems.add(new RouterItem("NeRtcActivity", ARouterPath.NeRtc.Audio));
        // Flutter
        //routerItems.add(new RouterItem("FlutterActivity", ARouterPath.Flutter.Flutter));
        return routerItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);

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
                L.i(TAG, "addIdleHandler: queueIdle " + Thread.currentThread().getName());
                return false;
            }
        });
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