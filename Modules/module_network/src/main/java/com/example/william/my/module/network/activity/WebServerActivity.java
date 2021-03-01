package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.utils.NetworkUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.network.socket.SocketServer;
import com.example.william.my.module.network.socket.SocketService;
import com.example.william.my.module.router.ARouterPath;

/**
 * http://www.websocket-test.com/
 * https://github.com/TooTallNate/Java-WebSocket
 */
@Route(path = ARouterPath.NetWork.NetWork_WebServer)
public class WebServerActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        String IpAddress = "ws://" + NetworkUtils.getIPAddress(true) + ":" + SocketServer.DEFAULT_SERVER_PORT;
        mResponse.setText(IpAddress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SocketService.startService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketService.stopService(this);
    }
}