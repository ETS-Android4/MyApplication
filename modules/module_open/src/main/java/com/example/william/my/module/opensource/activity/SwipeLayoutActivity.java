package com.example.william.my.module.opensource.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.opensource.adapter.SwipeRecyclerAdapter;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/daimajia/AndroidSwipeLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SwipeLayout)
public class SwipeLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_swipe_layout);

        List<String> mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("item : " + (i + 1));
        }

        RecyclerView mRecycleView = findViewById(R.id.swipe_recycleView);

        SwipeRecyclerAdapter mAdapter = new SwipeRecyclerAdapter(mData);
        //mAdapter.setMode(Attributes.Mode.Multiple);//默认为Single

        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}
