package com.netease.audioroom.demo.base;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.PermissionUtils;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.widget.loadsir.callback.BaseCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.FailureCallback;
import com.netease.audioroom.demo.widget.loadsir.core.LoadService;
import com.netease.audioroom.demo.widget.loadsir.core.LoadSir;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

public abstract class BaseActivity extends AppCompatActivity {

    private LoadService<?> loadService;//提示页面

    // 权限控制
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WAKE_LOCK};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerObserver(true);
        setContentView(getContentViewID());
        loadService = LoadSir.getDefault().register(BaseActivityManager.getInstance().getCurrentActivity());
    }

    @Override
    protected void onDestroy() {
        registerObserver(false);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

    private void registerObserver(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            @Override
            public void onEvent(StatusCode statusCode) {
                Log.i(BaseActivityManager.getInstance().getCurrentActivityName(), "login status  , code = " + statusCode);
            }
        }, register);
    }

    //加载页面
    protected abstract int getContentViewID();

    protected void requestLivePermission() {
        PermissionUtils.permission(
                LIVE_PERMISSIONS
        ).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied() {

            }
        }).request();
    }

    protected void showError() {
        loadShowCallback(FailureCallback.class);
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

    protected static VoiceRoomUser createUser() {
        AccountInfo accountInfo = DemoCache.getAccountInfo();
        return new VoiceRoomUser(accountInfo.account, accountInfo.nick, accountInfo.avatar);
    }
}
