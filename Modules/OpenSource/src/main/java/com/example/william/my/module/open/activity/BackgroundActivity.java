package com.example.william.my.module.open.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;
import com.noober.background.drawable.DrawableCreator;

/**
 * https://github.com/JavaNoober/BackgroundLibrary
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Background)
public class BackgroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_background);

        Drawable drawable = new DrawableCreator.Builder()
                .setSolidColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build();
    }
}