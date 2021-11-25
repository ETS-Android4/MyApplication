package com.example.william.my.module.demo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.adapter.RecyclerAdapter;
import com.example.william.my.module.router.ARouterPath;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Demo.Demo_FlexBox)
public class FlexBoxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_flexbox);

        RecyclerView recyclerView = findViewById(R.id.flex_recycleView);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        manager.setFlexDirection(FlexDirection.ROW);//主轴方向
        manager.setFlexWrap(FlexWrap.WRAP);//是否换行

        recyclerView.setLayoutManager(manager);
        recyclerView.setClipToPadding(false);

        List<String> mData = new ArrayList<>();
        mData.add("module_123");
        mData.add("module_456");
        mData.add("module_789");
        mData.add("module_1234");
        mData.add("module_5678");
        mData.add("module_12345");
        mData.add("module_67890");
        mData.add("module_123456");
        mData.add("module_456789");
        recyclerView.setAdapter(new RecyclerAdapter(mData));
    }
}