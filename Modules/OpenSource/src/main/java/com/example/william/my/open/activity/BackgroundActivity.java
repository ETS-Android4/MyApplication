package com.example.william.my.open.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.open.R;

/**
 * https://github.com/JavaNoober/BackgroundLibrary
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Background)
public class BackgroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_background);
    }
}