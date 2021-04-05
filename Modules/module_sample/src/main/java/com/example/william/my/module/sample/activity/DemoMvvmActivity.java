package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FragmentUtils;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.fragment.MvvmFragment;

public class DemoMvvmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout_fragment);

        // Add product list fragment if this is first creation
        MvvmFragment mvvmFragment = (MvvmFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (mvvmFragment == null) {
            mvvmFragment = MvvmFragment.newInstance();
            FragmentUtils.add(getSupportFragmentManager(), mvvmFragment, R.id.contentFrame);
        }
    }
}