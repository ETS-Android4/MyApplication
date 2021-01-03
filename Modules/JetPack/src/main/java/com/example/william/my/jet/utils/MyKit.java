package com.example.william.my.jet.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.example.william.my.jet.database.api.RoomApi;
import com.example.william.my.jet.database.table.OAuth;

public class MyKit {

    private static OAuth mOAuth;

    /**
     * 是否是登录状态
     *
     * @return isLogin
     */
    public static boolean isLogin() {
        return mOAuth != null && !TextUtils.isEmpty(mOAuth.getId());
    }

    public static OAuth getOAuth() {
        if (mOAuth == null) {
            return new OAuth("", "", 0);
        }
        return mOAuth;
    }

    public static void setOAuth(OAuth oAuth) {
        mOAuth = oAuth;
    }

    /**
     * @return 检查是否登录，如果未登录，则跳转登录页面
     */
    public boolean checkLogin(Context context) {
        if (isLogin()) {
            return true;
        } else {
            if (context instanceof Activity) {
                //ARouter.getInstance().build(Account.ACCOUNT_LOGIN).navigation((Activity) context, Config.REQUEST_LOGIN);
            } else {
                //ARouter.getInstance().build(Account.ACCOUNT_LOGIN).navigation(context);
            }
            return false;
        }
    }

    /**
     * 退出登录
     */
    public static void clear() {
        //删除登录信息
        RoomApi.getInstance().deleteOAuth();
        mOAuth = null;
    }

    public static void logout() {
        clear();
        //ARouter.getInstance().build(Account.MAIN).withInt(Account.MAIN, Config.MainExtra.LOGOUT).navigation();
    }
}
