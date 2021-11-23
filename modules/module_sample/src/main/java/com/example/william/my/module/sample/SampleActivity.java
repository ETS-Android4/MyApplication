package com.example.william.my.module.sample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.router.fragment.RouterRecyclerFragment;
import com.example.william.my.module.router.item.RouterItem;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;

@Route(path = ARouterPath.Sample.Sample)
public class SampleActivity extends BaseFragmentActivity {

    @Override
    public Fragment setFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("router", buildRouter());
        RouterRecyclerFragment fragment = new RouterRecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        routerItems.add(new RouterItem("MvpActivity", ARouterPath.Sample.Sample_MVP));
        routerItems.add(new RouterItem("MvvmActivity", ARouterPath.Sample.Sample_MVVM));
        routerItems.add(new RouterItem("KotlinActivity", ARouterPath.Sample.Sample_Kotlin));
        routerItems.add(new RouterItem("KotlinBindActivity", ARouterPath.Sample.Sample_Kotlin_Bind));
        return routerItems;
    }
}