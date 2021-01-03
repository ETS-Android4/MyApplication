package com.example.william.my.open.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.event.MessageEvent;
import com.example.william.my.module.router.ARouterPath;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 1.定义事件
 * 2.定义事件处理器
 * 3.注册EventBus事件
 * 4.发布EventBus事件
 * https://github.com/greenrobot/EventBus
 * https://greenrobot.org/eventbus/documentation/subscriber-index
 */
@Route(path = ARouterPath.OpenSource.OpenSource_EventBus)
public class EventBusActivity extends ResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        EventBus.getDefault().post(new MessageEvent("EventBus"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        showResponse(event.getMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}