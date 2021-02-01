package com.example.william.my.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.utils.NetworkUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.network.netty.server.NettyServer;
import com.example.william.my.network.netty.server.NettyService;
import com.example.william.my.network.socket.SocketServer;

/**
 * https://github.com/netty/netty/
 */
@Route(path = ARouterPath.NetWork.NetWork_Netty)
public class NettyActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        String IpAddress = "ws://" + NetworkUtils.getIPAddress(true) + ":" + NettyServer.DEFAULT_SERVER_PORT;
        mResponse.setText(IpAddress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NettyService.startService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NettyService.stopService(this);
    }
}