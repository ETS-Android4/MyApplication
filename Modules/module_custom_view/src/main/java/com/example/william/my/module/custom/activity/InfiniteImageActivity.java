package com.example.william.my.module.custom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView_InfiniteImage)
public class InfiniteImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_infinite_image);
    }
}