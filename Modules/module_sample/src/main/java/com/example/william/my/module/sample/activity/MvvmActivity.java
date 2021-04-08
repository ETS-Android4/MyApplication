package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FragmentUtils;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.fragment.MvvmFragment;

@Route(path = ARouterPath.Sample.Sample_MVVM)
public class MvvmActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout_fragment);

        // Add product list fragment if this is first creation
        MvvmFragment mvvmFragment = (MvvmFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (mvvmFragment == null) {
            mvvmFragment = MvvmFragment.newInstance();
            FragmentUtils.add(getSupportFragmentManager(), mvvmFragment, R.id.contentFrame);
        }
    }
}