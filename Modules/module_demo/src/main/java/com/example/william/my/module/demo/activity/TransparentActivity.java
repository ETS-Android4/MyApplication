package com.example.william.my.module.demo.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_Transparent)
public class TransparentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_activity_transparent);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Point point = new Point();
        point.x = metrics.widthPixels;
        point.y = metrics.heightPixels;

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (point.x * 0.5);
        params.height = (int) (point.y * 0.5);
        params.gravity = Gravity.START | Gravity.TOP;
        params.dimAmount = 0.1f;
        getWindow().setAttributes(params);
    }
}