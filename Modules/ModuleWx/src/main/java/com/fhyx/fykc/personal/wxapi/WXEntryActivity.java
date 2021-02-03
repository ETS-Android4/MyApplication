package com.fhyx.fykc.personal.wxapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.module.wx.R;
import com.example.william.my.module.wx.base.Config;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信登录，接收响应
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_activity_wx_entry);

        //通过WXAPIFactory工厂获取IWXApI的示例
        IWXAPI api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        //将应用的app id注册到微信
        api.registerApp(Config.APP_ID);
        //将接受信息传递给handleIntent方法
        api.handleIntent(getIntent(), this);
    }

    /**
     * 处理接收微信的请求
     */
    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 处理返回消息的回调
     */
    @Override
    public void onResp(BaseResp baseResp) {
        SendAuth.Resp resp = (SendAuth.Resp) baseResp;
        Log.e(TAG, resp.code);
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    }
}
