package com.example.william.my.module.wx.presenter;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxPresenter {

    private static final String TAG = "WxPresenter";

    // 第三方app和微信通信的openApi接口
    private IWXAPI api;

    public IWXAPI getApi() {
        return api;
    }

    /**
     * 注册
     */
    public void regToWx(Context context, final String APP_ID) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        //context.registerReceiver(new BroadcastReceiver() {
        //    @Override
        //    public void onReceive(Context context, Intent intent) {
        //
        //        // 将该app注册到微信
        //        api.registerApp(APP_ID);
        //    }
        //}, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    /**
     * 登录
     */
    public void loginWx(Context context, IWXAPI iwxapi) {
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(context, "您未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_login";
        iwxapi.sendReq(req);
    }
}
