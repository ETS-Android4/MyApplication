package com.example.william.my.module.demo.activity.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_Shapeable)
public class ShapeableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_shapeable);
    }
}