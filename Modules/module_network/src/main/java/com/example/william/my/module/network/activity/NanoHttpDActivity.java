package com.example.william.my.module.network.activity;

import android.text.util.Linkify;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.network.nano.NanoServer;
import com.example.william.my.module.network.nano.NanoService;
import com.example.william.my.module.network.utils.NetworkUtils;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/NanoHttpd/nanohttpd
 */
@Route(path = ARouterPath.NetWork.NetWork_NanoHttpD)
public class NanoHttpDActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        String ipAddress = "http://" + NetworkUtils.getIPAddress(true) + ":" + NanoServer.DEFAULT_SERVER_PORT;
        mResponse.setAutoLinkMask(Linkify.WEB_URLS);
        mResponse.setText(ipAddress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NanoService.startService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NanoService.stopService(this);
    }
}