package com.example.william.my.module.jiguang.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.william.my.module.jiguang.R;

import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;

/**
 * http://docs.jiguang.cn/jverification/client/android_api/
 */
public class JVerifyUIConfigUtil {

    public enum Direction {
        portrait, landscape, dialogPortrait, dialogLandscape,
    }

    public static JVerifyUIConfig getConfig(Context context, Direction direction) {

        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder();

        if (direction == Direction.dialogPortrait || direction == Direction.dialogLandscape) {
            int widthDp = px2dp(getScreenWidth(context));
            int heightDp = px2dp(getScreenHeight(context));
            int dialogWidth = direction == Direction.dialogPortrait ? (widthDp - 60) : 480;
            int dialogHeight = direction == Direction.dialogLandscape ? (heightDp - 100) : 300;
            uiConfigBuilder.setDialogTheme(dialogWidth, dialogHeight, 0, 0, false);
        }

        //设置页面背景
        //uiConfigBuilder.setAuthBGImgPath("main_bg")

        //隐藏状态栏
        uiConfigBuilder.setStatusBarHidden(direction == Direction.landscape);

        //导航
        if (direction == Direction.portrait || direction == Direction.landscape) {
            uiConfigBuilder.setNavTransparent(true);
            //uiConfigBuilder.setNavText("登录");
            //uiConfigBuilder.setNavTextColor(0xff6200EE);
            uiConfigBuilder.setNavReturnImgPath("j_btn_back");
        }

        //logo
        uiConfigBuilder.setLogoWidth(48);
        uiConfigBuilder.setLogoHeight(48);
        uiConfigBuilder.setLogoImgPath("ic_launcher");
        uiConfigBuilder.setLogoOffsetY(direction == Direction.portrait ? 110 : 0);

        //设置手机号文字颜色
        uiConfigBuilder.setNumberColor(0xFF222328);
        //设置手机号文字大小
        uiConfigBuilder.setNumberSize(24);
        //设置手机号向下偏移
        uiConfigBuilder.setNumFieldOffsetY(direction == Direction.portrait ? 190 : 100);

        //天翼账号提供认证服务
        uiConfigBuilder.setSloganTextSize(10);
        uiConfigBuilder.setSloganTextColor(0xFF3700B3);
        uiConfigBuilder.setSloganOffsetY(direction == Direction.portrait ? 235 : 135);

        //设置登录按钮
        uiConfigBuilder.setLogBtnImgPath("selector_btn_normal");
        uiConfigBuilder.setLogBtnTextColor(0xFFFFFFFF);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnWidth(direction == Direction.portrait ? 300 : 240);
        uiConfigBuilder.setLogBtnHeight(direction == Direction.portrait ? 48 : 40);
        uiConfigBuilder.setLogBtnOffsetY(direction == Direction.portrait ? 265 : 160);
        //登录按钮是否显示
        uiConfigBuilder.setPrivacyState(true);

        //设置底部条款基本文字颜色和协议文字颜色
        uiConfigBuilder.setPrivacyText("登录即同意《", " 》 《", "", "》并授权极光认证Demo获取本机号码");
        uiConfigBuilder.setAppPrivacyOne("应用自定义服务条款一", "https://www.jiguang.cn/about");
        uiConfigBuilder.setAppPrivacyTwo("应用自定义服务条款二", "https://www.jiguang.cn/about");
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5, 0xFF8998FF);
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);

        uiConfigBuilder.setPrivacyTextSize(10);
        uiConfigBuilder.setPrivacyOffsetY(18);


        // 手机登录按钮
        if (direction == Direction.portrait || direction == Direction.landscape) {
            RelativeLayout.LayoutParams layoutParamPhoneLogin = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParamPhoneLogin.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

            if (direction == Direction.portrait) {
                layoutParamPhoneLogin.setMargins(0, dp2px(340f), 0, 0);
                TextView tvPhoneLogin = new TextView(context);
                tvPhoneLogin.setText("手机号码登录");
                tvPhoneLogin.setLayoutParams(layoutParamPhoneLogin);
                uiConfigBuilder.addCustomView(tvPhoneLogin, false, new JVerifyUIClickCallback() {
                    @Override
                    public void onClicked(Context context, View view) {

                    }
                });
            } else {
                layoutParamPhoneLogin.setMargins(0, dp2px(15.0f), dp2px(15.0f), 0);
                layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                TextView tvPhoneLogin = new TextView(context);
                tvPhoneLogin.setText("手机号码登录");
                tvPhoneLogin.setLayoutParams(layoutParamPhoneLogin);
                uiConfigBuilder.addNavControlView(tvPhoneLogin, new JVerifyUIClickCallback() {
                    @Override
                    public void onClicked(Context context, View view) {

                    }
                });
            }
        }

        if (direction == Direction.portrait || direction == Direction.landscape) {
            // 微信qq新浪登录
            LinearLayout layoutLoginGroup = new LinearLayout(context);
            RelativeLayout.LayoutParams layoutLoginGroupParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutLoginGroupParam.setMargins(0, dp2px(direction == Direction.portrait ? 450f : 230f), 0, 0);
            layoutLoginGroupParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutLoginGroupParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutLoginGroupParam.setLayoutDirection(LinearLayout.HORIZONTAL);
            layoutLoginGroup.setLayoutParams(layoutLoginGroupParam);

            ImageView btnWechat = new ImageView(context);
            btnWechat.setImageResource(R.drawable.j_ic_wechat);
            btnWechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //JShareInterface.authorize(Wechat.Name, mAuthListener);
                }
            });

            ImageView btnQQ = new ImageView(context);
            btnQQ.setImageResource(R.drawable.j_ic_qqx);
            btnQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //JShareInterface.authorize(QQ.Name, mAuthListener);
                }
            });

            ImageView btnXinlang = new ImageView(context);
            btnXinlang.setImageResource(R.drawable.j_ic_weibo);
            btnXinlang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //JShareInterface.authorize(SinaWeibo.Name, mAuthListener);
                }
            });

            LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            btnParam.setMargins(25, 0, 25, 0);
            layoutLoginGroup.addView(btnWechat, btnParam);
            layoutLoginGroup.addView(btnQQ, btnParam);
            layoutLoginGroup.addView(btnXinlang, btnParam);
            uiConfigBuilder.addCustomView(layoutLoginGroup, false, null);
        }

        if (direction == Direction.dialogPortrait || direction == Direction.dialogLandscape) {
            // 关闭按钮
            ImageView closeButton = new ImageView(context);
            RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            btnParams.setMargins(0, dp2px(10.0f), dp2px(10), 0);
            btnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            btnParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            closeButton.setLayoutParams(btnParams);
            closeButton.setImageResource(R.drawable.j_btn_close);
            uiConfigBuilder.addCustomView(closeButton, true, null);
        }

        return uiConfigBuilder.build();
    }

    public static int getScreenWidth(Context context) {
        Point point = new Point();
        context.getDisplay().getRealSize(point);
        return point.x;
    }

    public static int getScreenHeight(Context context) {
        Point point = new Point();
        context.getDisplay().getRealSize(point);
        return point.y;
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

