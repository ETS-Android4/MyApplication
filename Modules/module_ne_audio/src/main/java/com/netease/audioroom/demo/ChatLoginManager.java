package com.netease.audioroom.demo;


import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.base.action.ILoginAction;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class ChatLoginManager implements ILoginAction {

    private static final String TAG = "LoginManager";

    private static boolean isLogin = false;

    private static final ChatLoginManager instance = new ChatLoginManager();

    private ChatLoginManager() {

    }

    public static ChatLoginManager getInstance() {
        return instance;
    }

    @Override
    public void tryLogin() {
        final AccountInfo accountInfo = DemoCache.getAccountInfo();
        if (accountInfo == null) {
            fetchLoginAccount(null);
            return;
        }
        Log.i(TAG, "nim login: account = " + accountInfo.account + " token = " + accountInfo.token);
        LoginInfo loginInfo = new LoginInfo(accountInfo.account, accountInfo.token);
        //AbortableFuture<LoginInfo> future = NIMClient.getService(AuthService.class).login(loginInfo);
        AbortableFuture<LoginInfo> future = NIMSDK.getAuthService().login(loginInfo);
        future.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo info) {
                Log.i(TAG, "nim login success");
                afterLogin(accountInfo);
                callback.onSuccess(accountInfo);
            }

            @Override
            public void onFailed(int i) {
                Log.i(TAG, "nim login failed:" + " code = " + i);
                fetchLoginAccount(accountInfo.account);
                isLogin = false;

            }

            @Override
            public void onException(Throwable throwable) {
                fetchLoginAccount(accountInfo.account);
                isLogin = false;

            }
        });
    }

    private void fetchLoginAccount(String preAccount) {
        ChatRoomHttpClient.getInstance().fetchAccount(preAccount, new ChatRoomHttpClient.ChatRoomHttpCallback<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                login(accountInfo);
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                ToastUtils.showShort("获取登录帐号失败 ， code = " + code);
                callback.onFailed(code, errorMsg);
                isLogin = false;
            }
        });
    }

    private void login(final AccountInfo accountInfo) {
        Log.i(TAG, "nim login:" + " account = " + accountInfo.account + " token = " + accountInfo.token);
        LoginInfo loginInfo = new LoginInfo(accountInfo.account, accountInfo.token);
        //AbortableFuture<LoginInfo> future = NIMClient.getService(AuthService.class).login(loginInfo);
        AbortableFuture<LoginInfo> future = NIMSDK.getAuthService().login(loginInfo);
        future.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo info) {
                Log.i(TAG, "nim login success");
                afterLogin(accountInfo);
                callback.onSuccess(accountInfo);
            }

            @Override
            public void onFailed(int i) {
                Log.i(TAG, "nim login failed:" + " code = " + i);
                callback.onFailed(i, "SDK登录失败");
                ToastUtils.showShort("SDK登录失败 , code = " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastUtils.showShort("SDK登录异常 , e = " + throwable);
                callback.onFailed(throwable.hashCode(), "SDK登录异常");
            }
        });
    }

    private void afterLogin(AccountInfo accountInfo) {
        isLogin = true;
        DemoCache.setAccountId(accountInfo.account);
        DemoCache.saveAccountInfo(accountInfo);
        Log.i(TAG, "after login  , account = " + accountInfo.account + " , nick = " + accountInfo.nick);
    }

    public void logout() {
        //NIMClient.getService(AuthService.class).logout();
        NIMSDK.getAuthService().logout();
    }

    public static boolean isLogin() {
        return isLogin;
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onSuccess(AccountInfo accountInfo);

        void onFailed(int code, String errorMsg);
    }
}
