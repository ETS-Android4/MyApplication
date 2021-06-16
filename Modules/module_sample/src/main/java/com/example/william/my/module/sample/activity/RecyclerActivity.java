package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.fragment.RecyclerFragment;

@Route(path = ARouterPath.Sample.Sample_Recycler)
public class RecyclerActivity extends BaseFragmentActivity {

    @Override
    public Fragment setFragment() {
        Fragment fragment = new RecyclerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", "value");
        fragment.setArguments(bundle);
        return fragment;
    }
}