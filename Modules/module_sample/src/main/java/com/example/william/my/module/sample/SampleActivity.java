package com.example.william.my.module.sample;

import android.view.View;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.fragment.RecyclerDialogFragment;

@Route(path = ARouterPath.Sample.Sample)
public class SampleActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("MvpActivity", ARouterPath.Sample.Sample_MVP);
        mMap.put("MvvmActivity", ARouterPath.Sample.Sample_MVVM);
        mMap.put("KotlinActivity", ARouterPath.Sample.Sample_Kotlin);
        mMap.put("KotlinBindActivity", ARouterPath.Sample.Sample_Kotlin_Bind);
        mMap.put("RecyclerActivity", ARouterPath.Sample.Sample_Recycler);
        mMap.put("RecyclerDialogFragment", "RecyclerDialogFragment");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if ("RecyclerDialogFragment".equals(mMap.get(mData.get(i)))) {
            RecyclerDialogFragment dialog = new RecyclerDialogFragment();
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        } else {
            super.onItemClick(adapterView, view, i, l);
        }
    }
}