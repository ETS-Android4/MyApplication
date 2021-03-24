package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.RecyclerAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Sample.Sample_FlexBox)
public class FlexBoxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_flexbox);

        RecyclerView recyclerView = findViewById(R.id.flex_recycleView);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        manager.setFlexDirection(FlexDirection.ROW);//主轴方向
        manager.setFlexWrap(FlexWrap.WRAP);//是否换行

        recyclerView.setLayoutManager(manager);
        recyclerView.setClipToPadding(false);

        List<String> mData = new ArrayList<>();
        mData.add("module_android_util");
        mData.add("module_bugly");
        mData.add("module_custom_view");
        mData.add("module_flutter");
        mData.add("module_jetpack");
        mData.add("module_jiguang");
        mData.add("module_kotlin");
        mData.add("module_network");
        mData.add("module_open_source");
        mData.add("module_sample");
        mData.add("module_sophix");
        mData.add("module_tencent_im");
        mData.add("module_wechat");
        recyclerView.setAdapter(new RecyclerAdapter(mData));
    }
}