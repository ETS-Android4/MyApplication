package com.example.william.my.module.demo.activity;

import android.os.Bundle;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_ViewFlipper)
public class ViewFlipperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_view_flipper);

        ViewFlipper mViewFlipper = findViewById(R.id.viewFlipper);
        mViewFlipper.startFlipping();
    }
}