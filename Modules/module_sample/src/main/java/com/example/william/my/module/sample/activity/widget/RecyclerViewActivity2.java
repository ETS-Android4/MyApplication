package com.example.william.my.module.sample.activity.widget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.RecyclerAdapter2;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Sample.Sample_RecyclerView2)
public class RecyclerViewActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_recycler);
        RecyclerView mRecyclerView = findViewById(R.id.recycleView);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        List<String> mData = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            mData.add("POSITION " + i);
        }

        RecyclerAdapter2 mRecyclerAdapter = new RecyclerAdapter2(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}