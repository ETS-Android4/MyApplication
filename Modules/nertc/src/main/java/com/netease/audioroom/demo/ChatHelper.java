package com.netease.audioroom.demo;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

public class ChatHelper {

    /**
     * 登陆
     */
    public static void login() {

    }

    /**
     * 退出登录
     */
    public static void logout() {
        NIMClient.getService(AuthService.class).logout();
    }
}
