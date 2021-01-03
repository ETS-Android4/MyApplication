package com.example.william.my.network.activity;

import android.text.util.Linkify;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.utils.NetworkUtils;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.network.nano.HttpServer;
import com.example.william.my.network.nano.HttpService;

/**
 * https://github.com/NanoHttpd/nanohttpd
 */
@Route(path = ARouterPath.NetWork.NetWork_NanoHttpD)
public class NanoHttpDActivity extends ResponseActivity {

    @Override
    public void initView() {
        super.initView();

        String IpAddress = "http://" + NetworkUtils.getIPAddress(true) + ":" + HttpServer.DEFAULT_SERVER_PORT;
        mResponse.setAutoLinkMask(Linkify.WEB_URLS);
        mResponse.setText(IpAddress);

        HttpService.startService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpService.stopService(this);
    }
}