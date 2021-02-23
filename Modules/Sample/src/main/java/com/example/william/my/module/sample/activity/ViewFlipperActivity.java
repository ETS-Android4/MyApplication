package com.example.william.my.module.sample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;

@Route(path = ARouterPath.Sample.Sample_ViewFlipper)
public class ViewFlipperActivity extends AppCompatActivity {

    private ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_view_flipper);
        mViewFlipper = findViewById(R.id.view_flipper_viewFlipper);
        mViewFlipper.startFlipping();
    }
}