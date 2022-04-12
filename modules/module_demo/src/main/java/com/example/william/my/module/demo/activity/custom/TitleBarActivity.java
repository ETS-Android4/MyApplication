package com.example.william.my.module.demo.activity.custom;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.titlebar.TitleBar;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.utils.T;

@Route(path = ARouterPath.Demo.Demo_TitleBar)
public class TitleBarActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_title_bar);
        TitleBar titleBar = TitleBar.setContentView(this, R.layout.demo_activity_title_bar);
        titleBar.setTitle("TITLE");
        titleBar.setBackPressed(true);//默认为true
        titleBar.setBackPressed(R.drawable.ic_launcher);
        titleBar.setBtnRight("菜单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.show("您点击了菜单");
            }
        });
    }
}