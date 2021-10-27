package com.example.william.my.module.demo.activity;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_Typeface)
public class TypefaceActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        AssetManager assets = getAssets();
        Typeface typeface = Typeface.createFromAsset(assets, "fonts/juice.ttf");
        mResponse.setTypeface(typeface);
    }
}