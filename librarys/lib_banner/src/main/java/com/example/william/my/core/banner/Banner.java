package com.example.william.my.core.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.william.my.core.banner.adapter.BannerAdapter;
import com.example.william.my.core.banner.indicator.Indicator;
import com.example.william.my.core.banner.listener.BannerOnPageChangeListener;
import com.example.william.my.core.banner.observer.BannerLifecycleObserver;
import com.example.william.my.core.banner.observer.BannerLifecycleObserverAdapter;
import com.example.william.my.core.banner.task.AutoLoopTask;
import com.example.william.my.core.banner.util.BannerUtils;

public class Banner<T, BA extends BannerAdapter<T, ? extends RecyclerView.ViewHolder>> extends FrameLayout implements BannerLifecycleObserver {

    private BA mAdapter;
    private ViewPager2 mViewPager2;

    private Indicator mIndicator;

    private BannerOnPageChangeCallback mPageChangeCallback;
    private BannerOnPageChangeListener mBannerChangeListener;

    private CompositePageTransformer mCompositePageTransformer;

    // 是否允许无限轮播（即首尾直接切换）
    private boolean mIsInfiniteLoop = true;

    // 轮播开始位置
    private int mStartPosition = 1;

    // 是否自动轮播
    private boolean mIsAutoLoop = false;
    // 轮播切换间隔时间
    public long mLoopTime = 1000;
    // 轮播任务
    public AutoLoopTask<T, BA> mLoopTask;

    // 是否要拦截事件
//    private boolean isIntercept = true;

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
        setInfiniteLoop();

        mViewPager2 = new ViewPager2(context);
        LayoutParams mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager2.setLayoutParams(mLayoutParams);
        addView(mViewPager2);

        mPageChangeCallback = new BannerOnPageChangeCallback();
        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback);

        mCompositePageTransformer = new CompositePageTransformer();
        mViewPager2.setPageTransformer(mCompositePageTransformer);
    }

    private void setInfiniteLoop() {
        // 当不支持无限循环时，要关闭自动轮播
        if (!isInfiniteLoop()) {
            isAutoLoop(false);
        }
        setStartPosition(isInfiniteLoop() ? mStartPosition : 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!getViewPager2().isUserInputEnabled()) {
            return super.dispatchTouchEvent(ev);
        }
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            start();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stop();
        }
        return super.dispatchTouchEvent(ev);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        if (!getViewPager2().isUserInputEnabled() || !isIntercept) {
//            return super.onInterceptTouchEvent(event);
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mStartX = event.getX();
//                mStartY = event.getY();
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float endX = event.getX();
//                float endY = event.getY();
//                float distanceX = Math.abs(endX - mStartX);
//                float distanceY = Math.abs(endY - mStartY);
//                if (getViewPager2().getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
//                    mIsViewPager2Drag = distanceX > mTouchSlop && distanceX > distanceY;
//                } else {
//                    mIsViewPager2Drag = distanceY > mTouchSlop && distanceY > distanceX;
//                }
//                getParent().requestDisallowInterceptTouchEvent(mIsViewPager2Drag);
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//        }
//        return super.onInterceptTouchEvent(event);
//    }

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

    class BannerOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

        private boolean isScrolled;
        private int mTempPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), position, getRealCount());
            if (mBannerChangeListener != null && realPosition == getCurrentItem() - 1) {
                mBannerChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
            if (getIndicator() != null && realPosition == getCurrentItem() - 1) {
                getIndicator().onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (isScrolled) {
                mTempPosition = position;
                int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), position, getRealCount());
                if (mBannerChangeListener != null) {
                    mBannerChangeListener.onPageSelected(realPosition);
                }
                if (getIndicator() != null) {
                    getIndicator().onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //手势滑动中,代码执行滑动中
            if (state == ViewPager2.SCROLL_STATE_DRAGGING ||
                    state == ViewPager2.SCROLL_STATE_SETTLING) {
                isScrolled = true;
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                //滑动闲置或滑动结束
                isScrolled = false;
                if (mTempPosition != -1 && isInfiniteLoop()) {
                    if (mTempPosition == 0) {
                        setCurrentItem(getRealCount(), false);
                    } else if (mTempPosition == getItemCount() - 1) {
                        setCurrentItem(1, false);
                    }
                }
            }
            if (mBannerChangeListener != null) {
                mBannerChangeListener.onPageScrollStateChanged(state);
            }
            if (getIndicator() != null) {
                getIndicator().onPageScrollStateChanged(state);
            }
        }
    }


    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (getItemCount() <= 1) {
                stop();
            } else {
                start();
            }
            setIndicatorPageChange();
        }
    };

    private void initIndicator() {
        if (getIndicator() == null || getAdapter() == null) {
            return;
        }
        removeIndicator();
        LayoutParams mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        addView(getIndicator().getIndicatorView(), mLayoutParams);
        setIndicatorPageChange();
    }

    public void removeIndicator() {
        if (getIndicator() != null) {
            removeView(getIndicator().getIndicatorView());
        }
    }

    public void setIndicatorPageChange() {
        if (getIndicator() != null) {
            int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), getCurrentItem(), getRealCount());
            getIndicator().onPageChanged(getRealCount(), realPosition);
        }
    }

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
     *
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
     * 返回banner页面总数
     */
    public int getItemCount() {
        if (getAdapter() != null) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    /**
     * 返回banner真实总数
     */
    public int getRealCount() {
        if (getAdapter() != null) {
            return getAdapter().getRealCount();
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
        setAdapter(adapter, true);
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
        initIndicator();
        return this;
    }

    /**
     * 设置轮播指示器
     */
    public Banner<T, BA> setIndicator(Indicator indicator) {
        removeIndicator();
        this.mIndicator = indicator;
        initIndicator();
        return this;
    }

    /**
     * 添加viewpager切换事件
     * <p>
     * 在viewpager2中切换事件{@link ViewPager2.OnPageChangeCallback}是一个抽象类，
     * 为了方便使用习惯这里用的是和viewpager一样的{@link ViewPager.OnPageChangeListener}接口
     * </p>
     */
    public Banner<T, BA> addBannerChangeListener(BannerOnPageChangeListener onPageChangeListener) {
        this.mBannerChangeListener = onPageChangeListener;
        return this;
    }

    /**
     * 设置PageTransformer
     * 和addPageTransformer不同，这个只支持一种transformer
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
    public Banner<T, BA> addPageTransformer(ViewPager2.PageTransformer transformer) {
        mCompositePageTransformer.addTransformer(transformer);
        return this;
    }

    public Banner<T, BA> removeTransformer(ViewPager2.PageTransformer transformer) {
        mCompositePageTransformer.removeTransformer(transformer);
        return this;
    }

    public Indicator getIndicator() {
        return mIndicator;
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
