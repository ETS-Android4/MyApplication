package com.example.william.my.module.demo.activity.sample;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_HandlerThread)
public class HandlerThreadActivity extends BaseResponseActivity {

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    @Override
    public void initView() {
        super.initView();

        //创建HandlerThread线程
        mHandlerThread = new HandlerThread("mHandlerThread");
        //开启HandlerThread线程
        mHandlerThread.start();

        //在HandlerThread线程中创建一个handler对象
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 这里接收Handler发来的消息，运行在handler_thread线程中
                switch (msg.what) {
                    case 1:
                        showResponse("收到主线程消息");
                        break;
                    case 2:
                        showResponse("收到子线程消息");
                        break;
                    default:
                        break;
                }
            }
        };
        //在主线程给Handler发送消息
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在子线程给Handler发送数据
                mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //终止HandlerThread运行
        mHandlerThread.quit();
    }
}