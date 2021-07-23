package com.netease.audioroom.demo.base;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.PermissionUtils;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomUser;

public abstract class BaseActivity extends AppCompatActivity {

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
    }

    @Override
    protected void onDestroy() {
        registerObserver(false);
        super.onDestroy();
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

    protected static VoiceRoomUser createUser() {
        AccountInfo accountInfo = DemoCache.getAccountInfo();
        return new VoiceRoomUser(accountInfo.account, accountInfo.nick, accountInfo.avatar);
    }
}
