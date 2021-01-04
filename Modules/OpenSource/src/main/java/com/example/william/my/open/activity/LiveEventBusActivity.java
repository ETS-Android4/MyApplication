package com.example.william.my.open.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.jeremyliao.liveeventbus.LiveEventBus;

/**
 * 消息TAG为一个String类型的消息名字
 * https://github.com/JeremyLiao/LiveEventBus
 */
@Route(path = ARouterPath.OpenSource.OpenSource_LiveEventBus)
public class LiveEventBusActivity extends ResponseActivity {

    @Override
    public void initView() {
        super.initView();

        LiveEventBus
                .get("some_key", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        showResponse(s);
                    }
                });
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        LiveEventBus
                .get("some_key")
                //.post("LiveEventBus")
                .postDelay("LiveEventBus", 1000);
    }
}