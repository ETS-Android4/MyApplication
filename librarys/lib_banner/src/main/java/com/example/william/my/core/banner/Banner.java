package com.example.william.my.core.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.viewpager2.widget.ViewPager2;

import com.example.william.my.core.banner.adapter.BannerAdapter;
import com.example.william.my.core.banner.task.AutoLoopTask;

public class Banner extends RelativeLayout {

    private BannerAdapter mAdapter;
    private ViewPager2 mViewPager2;

    // 轮播开始位置
    private int mStartPosition = 1;

    // 是否允许无限轮播（即首尾直接切换）
    private boolean mIsInfiniteLoop = true;



    // 是否自动轮播
    public boolean mIsAutoLoop = true;

    // 轮播切换间隔时间
    public long mLoopTime = 3000;

    public AutoLoopTask mLoopTask;


    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mLoopTask = new AutoLoopTask(this);
        mViewPager2 = new ViewPager2(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager2.setLayoutParams(params);
    }

//    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
//        @Override
//        public void onChanged() {
//            if (getItemCount() <= 1) {
//                stop();
//            } else {
//                start();
//            }
//            setIndicatorPageChange();
//        }
//    };

    /**
     * **********************************************************************
     * ------------------------ 对外公开API ---------------------------------*
     * **********************************************************************
     */


    public BannerAdapter getAdapter() {
        return mAdapter;
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    public int getItemCount() {
        if (getAdapter() != null) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    public int getCurrentItem() {
        return getViewPager2().getCurrentItem();
    }

    public Banner setCurrentItem(int position) {
        return setCurrentItem(position, true);
    }

    public Banner setCurrentItem(int position, boolean smoothScroll) {
        getViewPager2().setCurrentItem(position, smoothScroll);
        return this;
    }

    public boolean isInfiniteLoop() {
        return mIsInfiniteLoop;
    }

    public Banner setAdapter(BannerAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException(getContext().getString(R.string.banner_adapter_null_error));
        }
        this.mAdapter = adapter;
        if (!isInfiniteLoop()) {
            getAdapter().setIncreaseCount(0);
        }
        //getAdapter().registerAdapterDataObserver(mAdapterDataObserver);
        mViewPager2.setAdapter(adapter);
        setCurrentItem(mStartPosition, false);
        //initIndicator();
        return this;
    }
}
