package com.example.william.my.module.demo.activity.widget;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.decoration.RItemDecorationDivider;
import com.example.william.my.core.widget.decoration.RItemDecorationTop;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.adapter.RecyclerAdapter;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Demo.Demo_RecyclerView)
public class RecyclerViewActivity extends BaseActivity implements RecyclerAdapter.OnItemClickListener {

    private RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_recycler);
        RecyclerView mRecyclerView = findViewById(R.id.recycleView);

        //保持固定的大小，提高性能
        mRecyclerView.setHasFixedSize(true);
        //线性布局管理器
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //网格布局管理器
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 1);
        mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //瀑布流布局管理七七
        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        //设置item添加和移除的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        mRecyclerView.addItemDecoration(new RItemDecorationTop());
        mRecyclerView.addItemDecoration(new RItemDecorationDivider(this));

        /*
         * LinearSnapHelper,PagerSnapHelper 使RecyclerView 像ViewPager一样的效果，每次只能滑动一页
         * LinearSnapHelper 支持快速滑动
         * PagerSnapHelper 限制一次只能滑动一页，不能快速滑动
         */
        //LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        //linearSnapHelper.attachToRecyclerView(mRecyclerView);

        //PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        //pagerSnapHelper.attachToRecyclerView(mRecyclerView);

        LayoutAnimationController mController = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.widget_anim_item_left));
        //显示顺序：ORDER_NORMAL 顺序，ORDER_REVERSE 倒序，ORDER_RANDOM 随机
        mController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        //显示间隔
        mController.setDelay(0.2f);
        mRecyclerView.setLayoutAnimation(mController);

        List<String> mData = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            mData.add("POSITION " + i);
        }

        mRecyclerAdapter = new RecyclerAdapter(mData);
        //设置唯一标识符，需要在setAdapter之前调用
        mRecyclerAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        //设置点击事件
        mRecyclerAdapter.setOnItemClickListener(this);

        //设置ViewCacheExtension缓存
        //mRecyclerView.setViewCacheExtension(new RecyclerCacheExtension());
    }

    @Override
    public void onItemClick(RecyclerAdapter adapter, View view, int position) {
        adapter.notifyItemChanged(position, "payload");
    }
}