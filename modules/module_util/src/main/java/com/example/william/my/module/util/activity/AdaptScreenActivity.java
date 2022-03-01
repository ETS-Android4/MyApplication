package com.example.william.my.module.util.activity;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.util.R;

/**
 * 屏幕适配
 * https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
 * 头条的方案是直接修改 DisplayMetrics#density 的 dp 适配，这样会导致系统 View 尺寸和原先不一致。
 * AdaptScreenUtils 选择操控 pt (屏幕的物理尺寸)进行适配。
 */
@Route(path = ARouterPath.Util.Util_AdaptScreen)
public class AdaptScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_activity_adapt_screen);
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080);
    }
}