package com.example.william.my.module.widget.activity.function;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Widget.Widget_Typeface)
public class TypefaceActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        AssetManager assets = getAssets();
        Typeface typeface = Typeface.createFromAsset(assets, "juice.ttf");
        mResponse.setTypeface(typeface);
    }
}