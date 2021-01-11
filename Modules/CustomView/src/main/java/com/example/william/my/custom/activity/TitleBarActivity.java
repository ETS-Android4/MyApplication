package com.example.william.my.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.toolBar.TitleBar;
import com.example.william.my.custom.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView_TitleBar)
public class TitleBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_title_bar);
        TitleBar titleBar = TitleBar.setContentView(this, R.layout.custom_activity_title_bar);
        titleBar.setTitle(getClass().getSimpleName());
        titleBar.setBackPressed(true);//默认为true
        titleBar.setBackPressed(R.drawable.im_ic_launcher);
        titleBar.setBtnRight("菜单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TitleBarActivity.this, "您点击了菜单", Toast.LENGTH_SHORT).show();
            }
        });
    }
}