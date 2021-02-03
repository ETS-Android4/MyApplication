package com.example.william.my.module.open.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/CarGuo/GSYVideoPlayer
 */
@Route(path = ARouterPath.OpenSource.OpenSource_GSYPlayer)
public class GSYPlayerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_gsy_player);
    }
}