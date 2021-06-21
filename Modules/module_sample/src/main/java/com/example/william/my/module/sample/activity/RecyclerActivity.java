package com.example.william.my.module.sample.activity;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.fragment.RecyclerFragment;

@Route(path = ARouterPath.Sample.Sample_Recycler)
public class RecyclerActivity extends BaseFragmentActivity {

    @Override
    public Fragment setFragment() {
        return new RecyclerFragment();
    }
}