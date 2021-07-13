package com.example.william.my.module.open.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/airbnb/lottie-android
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Lottie)
public class LottieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_lottie);
    }
}