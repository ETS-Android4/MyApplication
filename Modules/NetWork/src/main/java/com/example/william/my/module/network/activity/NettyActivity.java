package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.network.netty.client.NettyClient;
import com.example.william.my.module.router.ARouterPath;

import java.util.concurrent.Executors;

/**
 * https://github.com/netty/netty/
 */
@Route(path = ARouterPath.NetWork.NetWork_Netty)
public class NettyActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        boolean b = NettyClient.getInstance().sendMessage(NettyClient.getInstance().getAddress());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                NettyClient.getInstance().connect("192.168.40.89", 8080);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NettyClient.getInstance().disconnect();
    }
}