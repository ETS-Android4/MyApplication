package com.example.william.my.wx;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.wx.base.Config;
import com.example.william.my.wx.presenter.WxPresenter;

@Route(path = ARouterPath.Module_WeChat)
public class WeChatActivity extends ResponseActivity {

    private WxPresenter mPresenter;


    @Override
    public void initView() {
        super.initView();

        mPresenter = new WxPresenter();
        //注册
        mPresenter.regToWx(this, Config.APP_ID);
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        //登录
        mPresenter.loginWx(WeChatActivity.this, mPresenter.getApi());
    }
}
