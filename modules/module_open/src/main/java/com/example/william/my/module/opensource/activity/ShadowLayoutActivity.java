package com.example.william.my.module.opensource.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/lihangleo2/ShadowLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_ShadowLayout)
public class ShadowLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_shadow_layout);
    }
}