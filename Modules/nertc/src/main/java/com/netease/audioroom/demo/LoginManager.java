package com.netease.audioroom.demo;


import com.google.gson.Gson;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.yunxin.kit.alog.ALog;

/**
 * 登陆管理器
 */
public class LoginManager {

    private static final String TAG = "LoginManager";

    private boolean isLogin = false;

    private static class InstanceHolder {
        private static final LoginManager instance = new LoginManager();
    }

    public static LoginManager getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 自动登录
     */
    public void tryLogin() {
        final AccountInfo accountInfo = DemoCache.getAccountInfo();
        ALog.e("TAG", new Gson().toJson(accountInfo));
        if (accountInfo == null) {
            fetchLoginAccount(null);
            return;
        }
        ALog.i("nim login: account = " + accountInfo.account + " token = " + accountInfo.token);
        LoginInfo loginInfo = new LoginInfo(accountInfo.account, accountInfo.token);
        //服务器
        AbortableFuture<LoginInfo> login = NIMSDK.getAuthService().login(loginInfo);
        //AbortableFuture<LoginInfo> login = NIMClient.getService(AuthService.class).login(loginInfo);
        login.setCallback(new RequestCallback<AccountInfo>() {

            @Override
            public void onSuccess(AccountInfo param) {
                ALog.i("nim login success");
                isLogin = true;
                afterLogin(accountInfo);
                callback.onSuccess(accountInfo);
            }

            @Override
            public void onFailed(int i) {
                ALog.i("nim login failed:" + " code = " + i);
                isLogin = false;
                fetchLoginAccount(accountInfo.account);
            }

            @Override
            public void onException(Throwable throwable) {
                ALog.e("nim login failed:" + " e = " + throwable.getMessage());
                isLogin = false;
                fetchLoginAccount(accountInfo.account);
            }
        });
    }

    /**
     * 获取登陆账户
     */
    private void fetchLoginAccount(String preAccount) {
        ChatRoomHttpClient.getInstance().fetchAccount(preAccount, new ChatRoomHttpClient.ChatRoomHttpCallback<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                login(accountInfo);
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                isLogin = false;
                ToastHelper.showToast("获取登录帐号失败 ， code = " + code);
                callback.onFailed(code, errorMsg);
            }
        });
    }


    private void login(final AccountInfo accountInfo) {
        ALog.i("nim login:" + " account = " + accountInfo.account + " token = " + accountInfo.token);
        LoginInfo loginInfo = new LoginInfo(accountInfo.account, accountInfo.token);
        AbortableFuture<LoginInfo> login = NIMSDK.getAuthService().login(loginInfo);
        //AbortableFuture<LoginInfo> login = NIMClient.getService(AuthService.class).login(loginInfo);
        login.setCallback(new RequestCallback<AccountInfo>() {

            @Override
            public void onSuccess(AccountInfo param) {
                ALog.i("nim login success");
                afterLogin(accountInfo);
                callback.onSuccess(accountInfo);
            }

            @Override
            public void onFailed(int i) {
                ALog.i("nim login failed:" + " code = " + i);
                ToastHelper.showToast("SDK登录失败 , code = " + i);
                callback.onFailed(i, "SDK登录失败");
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("SDK登录异常 , e = " + throwable);
                callback.onFailed(throwable.hashCode(), "SDK登录异常");
            }
        });
    }

    /**
     * 保存用户信息
     */
    private void afterLogin(AccountInfo accountInfo) {
        DemoCache.setAccountId(accountInfo.account);
        DemoCache.saveAccountInfo(accountInfo);
        ALog.i(TAG, "after login  , account = " + accountInfo.account + " , nick = " + accountInfo.nick);
    }

    public boolean isLogin() {
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
