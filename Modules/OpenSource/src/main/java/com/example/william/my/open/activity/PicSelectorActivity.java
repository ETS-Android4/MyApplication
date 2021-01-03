package com.example.william.my.open.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.open.R;

/**
 * https://github.com/LuckSiege/PictureSelector
 */
@Route(path = ARouterPath.OpenSource.OpenSource_PicSelector)
public class PicSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);
    }
}