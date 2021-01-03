package com.example.william.my.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.utils.NetworkUtils;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.network.nano.HttpServer;
import com.example.william.my.network.socket.SocketService;

/**
 * http://www.websocket-test.com/
 * https://github.com/TooTallNate/Java-WebSocket
 */
@Route(path = ARouterPath.NetWork.NetWork_WebServer)
public class WebServerActivity extends ResponseActivity {

    @Override
    public void initView() {
        super.initView();

        String IpAddress = "ws://" + NetworkUtils.getIPAddress(true) + ":" + HttpServer.DEFAULT_SERVER_PORT;
        mResponse.setText(IpAddress);

        SocketService.startService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketService.stopService(this);
    }
}