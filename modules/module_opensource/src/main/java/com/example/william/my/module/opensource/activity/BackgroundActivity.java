package com.example.william.my.module.opensource.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.noober.background.drawable.DrawableCreator;

/**
 * https://github.com/JavaNoober/BackgroundLibrary
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Background)
public class BackgroundActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_background);

        Drawable drawable = new DrawableCreator.Builder()
                .setSolidColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build();
    }
}