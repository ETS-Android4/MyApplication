package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.RecyclerAdapter;
import com.google.android.flexbox.AlignItems;
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
        for (int i = 1; i < 61; i++) {
            mData.add("POSITION " + i);
        }
        recyclerView.setAdapter(new RecyclerAdapter(mData));
    }
}