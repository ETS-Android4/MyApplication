package com.example.william.my.module.widget.activity;

import android.os.Bundle;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.widget.R;

@Route(path = ARouterPath.Widget.Widget_ViewFlipper)
public class ViewFlipperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_activity_view_flipper);

        ViewFlipper mViewFlipper = findViewById(R.id.viewFlipper);
        mViewFlipper.startFlipping();
    }
}