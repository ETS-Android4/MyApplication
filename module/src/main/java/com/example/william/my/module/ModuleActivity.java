package com.example.william.my.module;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.event.DefaultSmartEventBus;
import com.example.william.my.module.event.MessageEvent;
import com.example.william.my.module.event.SmartMessageEvent;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.fragment.RouterRecyclerFragment;
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
public class ModuleActivity extends BaseFragmentActivity {

    @Autowired(name = "param_key")
    public String mModuleParam;

    @Override
    public Fragment setFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("router", buildRouter());
        RouterRecyclerFragment fragment = new RouterRecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        //
        routerItems.add(new RouterItem("AndroidUtilActivity", ARouterPath.Util.Util));
        routerItems.add(new RouterItem("OpenSourceActivity", ARouterPath.OpenSource.OpenSource));
        //
        routerItems.add(new RouterItem("NetWorkActivity", ARouterPath.NetWork.NetWork));
        routerItems.add(new RouterItem("JetPackActivity", ARouterPath.JetPack.JetPack));
        routerItems.add(new RouterItem("KotlinActivity", ARouterPath.Kotlin.Kotlin));
        routerItems.add(new RouterItem("SampleActivity", ARouterPath.Sample.Sample));
        //
        routerItems.add(new RouterItem("LibrariesActivity", ARouterPath.Lib.Lib));
        routerItems.add(new RouterItem("DemoActivity", ARouterPath.Demo.Demo));
        // 网易云信直播间
        routerItems.add(new RouterItem("NeRtcActivity", ARouterPath.NeRtc.Audio));
        //
        routerItems.add(new RouterItem("FlutterActivity", ARouterPath.Flutter.Flutter));
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