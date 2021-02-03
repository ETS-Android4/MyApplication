package com.example.william.my.module.open.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.open.R;
import com.example.william.my.module.open.adapter.SwipeRecyclerAdapter;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/daimajia/AndroidSwipeLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SwipeLayout)
public class SwipeLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_layout_recycle);

        List<String> mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("item : " + (i + 1));
        }

        RecyclerView mRecycleView = findViewById(R.id.basics_recycleView);

        SwipeRecyclerAdapter mAdapter = new SwipeRecyclerAdapter(mData);
        //mAdapter.setMode(Attributes.Mode.Multiple);//默认为Single

        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}
