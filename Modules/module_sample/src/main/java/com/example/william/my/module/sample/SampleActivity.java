package com.example.william.my.module.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Sample.Sample)
public class SampleActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Basics_WindowAnimTheme_Bottom);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        mMap.put("MvpActivity", ARouterPath.Sample.Sample_MVP);
        mMap.put("MvvmActivity", ARouterPath.Sample.Sample_MVVM);
        mMap.put("KotlinActivity", ARouterPath.Sample.Sample_Kotlin);
        mMap.put("KotlinBindActivity", ARouterPath.Sample.Sample_Kotlin_Bind);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //super.onItemClick(adapterView, view, i, l);
        startActivity(new Intent(this, SampleActivity.class));
        //overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
    }

}