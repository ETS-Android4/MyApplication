package com.example.william.my.module.open.activity;

import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.event.DefaultSmartEventBus;
import com.example.william.my.module.event.SmartMessageEvent;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/JeremyLiao/SmartEventBus
 * https://github.com/JeremyLiao/SmartEventBus/blob/master/docs/bus_all.md
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SmartEventBus)
public class SmartEventBusActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        DefaultSmartEventBus
                .event1()
                .observe(this, new Observer<SmartMessageEvent>() {
                    @Override
                    public void onChanged(SmartMessageEvent event) {
                        showResponse(event.getMessage());
                    }
                });
    }

    @Override
    public void setOnClick() {
        super.setOnClick();

        DefaultSmartEventBus
                .event1()
                //.post(new SmartMessageEvent("SmartEventBus"));
                .postDelay(new SmartMessageEvent("SmartEventBus"), 1000);
    }
}