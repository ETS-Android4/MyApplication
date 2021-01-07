package com.example.william.my.sample.activity;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.decoration.RItemDecorationDivider;
import com.example.william.my.core.widget.decoration.RItemDecorationTop;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.R;
import com.example.william.my.sample.adapter.RAdapter;
import com.example.william.my.sample.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Sample.Sample_RecyclerView)
public class RecyclerViewActivity extends AppCompatActivity {

    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_recycler);
        RecyclerView mRecyclerView = findViewById(R.id.widget_recycleView);

        //保持固定的大小，提高性能
        mRecyclerView.setHasFixedSize(true);
        //线性布局管理器
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //网格布局管理器
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 1);
        mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //瀑布流布局管理七七
        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        //设置item添加和移除的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        mRecyclerView.addItemDecoration(new RItemDecorationTop());
        mRecyclerView.addItemDecoration(new RItemDecorationDivider(this));


        LayoutAnimationController mController = new LayoutAnimationController(
                AnimationUtils.loadAnimation(this, R.anim.sample_anim_view_left));
        //ORDER_NORMAL 顺序，ORDER_REVERSE 倒序，ORDER_RANDOM 随机
        mController.setOrder(LayoutAnimationController.ORDER_NORMAL);//显示顺序
        mController.setDelay(0.2f);//显示间隔
        mRecyclerView.setLayoutAnimation(mController);

        mData = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            mData.add("POSITION " + i);
        }
        mRecyclerView.setAdapter(new RAdapter<String>(this, mData,
                R.layout.sample_item_primary, R.layout.sample_item_primary_dark) {
            @Override
            public int setItemViewType(int position, String data) {
                if (position % 2 == 0) {
                    return R.layout.sample_item_primary;
                } else if (position % 2 == 1) {
                    return R.layout.sample_item_primary_dark;
                }
                return 0;
            }

            @Override
            public void convert(ViewHolder holder, String data, int position, boolean payloadsIsEmpty) {

            }
        });
        RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(this, mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}