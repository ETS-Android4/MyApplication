package com.example.william.my.module.custom.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView_ScrollPage)
public class ScrollPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_scroll_page);
    }
}