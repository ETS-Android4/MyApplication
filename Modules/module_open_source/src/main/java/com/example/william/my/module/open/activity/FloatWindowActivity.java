package com.example.william.my.module.open.activity;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;
import com.lzf.easyfloat.EasyFloat;

/**
 * https://github.com/princekin-f/EasyFloat
 */
@Route(path = ARouterPath.OpenSource.OpenSource_FloatWindow)
public class FloatWindowActivity extends BaseResponseActivity {

    @Override
    public void onClick(View v) {
        super.onClick(v);
        EasyFloat.with(this)
                .setLayout(R.layout.open_layout_float)
                .show();
    }
}