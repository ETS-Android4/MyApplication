package com.example.william.my.core.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.william.my.core.banner.adapter.BannerAdapter;
import com.example.william.my.core.banner.callback.BannerOnPageChangeCallback;
import com.example.william.my.core.banner.observer.BannerLifecycleObserver;
import com.example.william.my.core.banner.observer.BannerLifecycleObserverAdapter;
import com.example.william.my.core.banner.task.AutoLoopTask;

public class Banner<T, BA extends BannerAdapter<T, ? extends RecyclerView.ViewHolder>> extends RelativeLayout implements BannerLifecycleObserver {

    private BA mAdapter;
    private ViewPager2 mViewPager2;

    private BannerOnPageChangeCallback mPageChangeCallback;
    private CompositePageTransformer mCompositePageTransformer;

    // 是否允许无限轮播（即首尾直接切换）
    private boolean mIsInfiniteLoop = true;

    // 轮播开始位置
    private int mStartPosition = 1;

    // 是否自动轮播
    private boolean mIsAutoLoop = true;
    // 轮播切换间隔时间
    public long mLoopTime = 3000;
    // 轮播任务
    public AutoLoopTask<T, BA> mLoopTask;

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mLoopTask = new AutoLoopTask<>(this);

        mViewPager2 = new ViewPager2(context);
        LayoutParams mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager2.setLayoutParams(mLayoutParams);
        addView(mViewPager2);

        mPageChangeCallback = new BannerOnPageChangeCallback();
        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback);

        mCompositePageTransformer = new CompositePageTransformer();
        mViewPager2.setPageTransformer(mCompositePageTransformer);

        setInfiniteLoop();
    }

    private void setInfiniteLoop() {
        // 当不支持无限循环时，要关闭自动轮播
        if (!isInfiniteLoop()) {
            isAutoLoop(false);
        }
        setStartPosition(isInfiniteLoop() ? mStartPosition : 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (getItemCount() <= 1) {
                stop();
            } else {
                start();
            }
            //setIndicatorPageChange();
        }
    };

    /*
     * **********************************************************************
     * ------------------------ 对外公开API ---------------------------------*
     * **********************************************************************
     */

    public boolean isInfiniteLoop() {
        return mIsInfiniteLoop;
    }

    /**
     * 设置开始的位置 (需要在setAdapter或者setData之前调用才有效哦)
     */
    public Banner<T, BA> setStartPosition(int mStartPosition) {
        this.mStartPosition = mStartPosition;
        return this;
    }

    /**
     * 是否允许自动轮播
     * @return
     */
    public boolean isAutoLoop() {
        return mIsAutoLoop;
    }

    /**
     * 是否允许自动轮播
     *
     * @param isAutoLoop ture 允许，false 不允许
     */
    public Banner<T, BA> isAutoLoop(boolean isAutoLoop) {
        this.mIsAutoLoop = isAutoLoop;
        return this;
    }

    public BA getAdapter() {
        return mAdapter;
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    /**
     * 当前所选页面
     */
    public int getCurrentItem() {
        return getViewPager2().getCurrentItem();
    }

    /**
     * Banner页面数量
     */
    public int getItemCount() {
        if (getAdapter() != null) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     *
     * @param position
     * @return
     */
    public Banner<T, BA> setCurrentItem(int position) {
        return setCurrentItem(position, true);
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     *
     * @param position
     * @param smoothScroll
     * @return
     */
    public Banner<T, BA> setCurrentItem(int position, boolean smoothScroll) {
        getViewPager2().setCurrentItem(position, smoothScroll);
        return this;
    }

    /**
     * 设置banner的适配器
     */
    public Banner<T, BA> setAdapter(BA adapter) {
        if (adapter == null) {
            throw new NullPointerException(getContext().getString(R.string.banner_adapter_null_error));
        }
        this.mAdapter = adapter;
        if (!isInfiniteLoop()) {
            getAdapter().setIncreaseCount(0);
        }
        getAdapter().registerAdapterDataObserver(mAdapterDataObserver);
        mViewPager2.setAdapter(adapter);
        setCurrentItem(mStartPosition, false);
        //initIndicator();
        return this;
    }

    /**
     * 设置banner的适配器
     *
     * @param adapter
     * @param isInfiniteLoop 是否支持无限循环
     * @return
     */
    public Banner<T, BA> setAdapter(BA adapter, boolean isInfiniteLoop) {
        mIsInfiniteLoop = isInfiniteLoop;
        setInfiniteLoop();
        setAdapter(adapter);
        return this;
    }

    /**
     * 设置PageTransformer，和addPageTransformer不同，这个只支持一种transformer
     */
    public Banner<T, BA> setPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        getViewPager2().setPageTransformer(transformer);
        return this;
    }

    /**
     * 添加PageTransformer，可以组合效果
     * {@link ViewPager2.PageTransformer}
     * 如果找不到请导入implementation "androidx.viewpager2:viewpager2:1.0.0"
     */
    public Banner<T, BA> addPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        mCompositePageTransformer.addTransformer(transformer);
        return this;
    }

    public Banner<T, BA> removeTransformer(ViewPager2.PageTransformer transformer) {
        mCompositePageTransformer.removeTransformer(transformer);
        return this;
    }

    /**
     * 开始轮播
     */
    public Banner<T, BA> start() {
        if (mIsAutoLoop) {
            stop();
            postDelayed(mLoopTask, mLoopTime);
        }
        return this;
    }

    /**
     * 停止轮播
     */
    public Banner<T, BA> stop() {
        if (mIsAutoLoop) {
            removeCallbacks(mLoopTask);
        }
        return this;
    }

    /**
     * 移除一些引用
     */
    public void destroy() {
        if (getViewPager2() != null && mPageChangeCallback != null) {
            getViewPager2().unregisterOnPageChangeCallback(mPageChangeCallback);
            mPageChangeCallback = null;
        }
        stop();
    }

    /**
     * **********************************************************************
     * ------------------------ 生命周期控制 --------------------------------*
     * **********************************************************************
     */

    public Banner<T, BA> addBannerLifecycleObserver(LifecycleOwner owner) {
        if (owner != null) {
            owner.getLifecycle().addObserver(new BannerLifecycleObserverAdapter(owner, this));
        }
        return this;
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        start();
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        stop();
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        destroy();
    }

}
