package com.example.william.my.custom.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.custom.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView_ScrollPage)
public class ScrollPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_scroll_page);
    }
}