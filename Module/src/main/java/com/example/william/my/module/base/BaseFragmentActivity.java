package com.example.william.my.module.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.R;

public abstract class BaseFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_activity_fragment);

        initView();
    }

    private void initView() {
        //创建FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //创建FragmentTransaction事务对象
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        //使用replace（将要替换位置的i的，替换的页面）方法实现页面的替换
        fragmentTransaction.replace(R.id.fragment, setFragment());
        //提交事务
        fragmentTransaction.commit();
    }

    public abstract Fragment setFragment();

}
