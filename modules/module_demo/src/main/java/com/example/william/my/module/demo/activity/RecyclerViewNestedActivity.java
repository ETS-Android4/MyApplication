package com.example.william.my.module.demo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.adapter.RecyclerNestedAdapter;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Demo.Demo_RecyclerViewNested)
public class RecyclerViewNestedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_recycler);
        RecyclerView mRecyclerView = findViewById(R.id.recycleView);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        List<String> mData = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            mData.add("POSITION " + i);
        }

        RecyclerNestedAdapter mRecyclerAdapter = new RecyclerNestedAdapter(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}