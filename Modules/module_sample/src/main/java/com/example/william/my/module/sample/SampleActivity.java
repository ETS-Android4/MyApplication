package com.example.william.my.module.sample;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Sample.Sample)
public class SampleActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("MvpActivity", ARouterPath.Sample.Sample_MVP);
        mMap.put("MvvmActivity", ARouterPath.Sample.Sample_MVVM);
        mMap.put("KotlinActivity", ARouterPath.Sample.Sample_Kotlin);
        mMap.put("KotlinBindActivity", ARouterPath.Sample.Sample_Kotlin_Bind);
    }
}