package com.netease.audioroom.demo.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.widget.loadsir.callback.BaseCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.ErrorCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.LoadingCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.NetErrCallback;
import com.netease.audioroom.demo.widget.loadsir.core.LoadService;
import com.netease.audioroom.demo.widget.loadsir.core.LoadSir;
import com.netease.audioroom.demo.widget.loadsir.core.Transport;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.yunxin.kit.alog.ALog;
import com.netease.yunxin.nertc.nertcvoiceroom.model.VoiceRoomUser;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 提示页面
     */
    protected LoadService loadService;

    /**
     * 监听登录状态
     */
    private Observer<StatusCode> onlineStatusObserver = statusCode -> onLoginEvent(statusCode);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerObserver(true);
        setContentView(getContentViewID());
        setupLoadService();
    }

    /**
     * 加载页面
     *
     * @return
     */
    protected abstract int getContentViewID();

    private void setupLoadService() {
        loadService = LoadSir.getDefault().register(BaseActivityManager.getInstance().getCurrentActivity());
    }

    @Override
    protected void onDestroy() {
        registerObserver(false);
        super.onDestroy();
    }

    protected void registerObserver(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(onlineStatusObserver, register);
    }

    protected void showNetError() {
        loadShowCallback(NetErrCallback.class);
    }

    protected void showError() {
        loadShowCallback(ErrorCallback.class);
    }

    protected void showLoading() {
        loadShowCallback(LoadingCallback.class);
    }

    protected void loadSuccess() {
        if (loadService != null) {
            loadService.showSuccess();
        }
    }

    public void loadShowCallback(Class<? extends BaseCallback> callback) {
        if (loadService != null) {
            loadService.showCallback(callback);
        }
    }

    public void setLoadCallBack(Class<? extends BaseCallback> callback, Transport transport) {
        loadService.setCallBack(callback, transport);
    }

    protected void onLoginEvent(StatusCode statusCode) {
        ALog.i(BaseActivityManager.getInstance().getCurrentActivityName(), "login status  , code = " + statusCode);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

    protected static VoiceRoomUser createUser() {
        AccountInfo accountInfo = DemoCache.getAccountInfo();
        return new VoiceRoomUser(accountInfo.account, accountInfo.nick, accountInfo.avatar);
    }
}
