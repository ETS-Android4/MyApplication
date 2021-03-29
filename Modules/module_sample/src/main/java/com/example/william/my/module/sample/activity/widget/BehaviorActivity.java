package com.example.william.my.module.sample.activity.widget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Sample.Sample_Behavior)
public class BehaviorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_behavior);

        RecyclerView mRecyclerView = findViewById(R.id.behavior_recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> mData = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            mData.add("POSITION " + i);
        }

        RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);

//        findViewById(R.id.behavior_button).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    v.setX(event.getRawX() - v.getWidth() / 2);
//                    v.setY(event.getRawY() - v.getHeight() / 2);
//                } else {
//                    v.performClick();
//                }
//                return false;
//            }
//        });
    }
}