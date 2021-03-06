package com.example.william.my.core.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.william.my.core.banner.adapter.BannerAdapter;
import com.example.william.my.core.banner.config.BannerConfig;
import com.example.william.my.core.banner.config.BannerIndicatorConfig;
import com.example.william.my.core.banner.config.BannerIndicatorMargins;
import com.example.william.my.core.banner.indicator.Indicator;
import com.example.william.my.core.banner.listener.BannerOnBannerListener;
import com.example.william.my.core.banner.listener.BannerOnPageChangeListener;
import com.example.william.my.core.banner.observer.BannerLifecycleObserver;
import com.example.william.my.core.banner.observer.BannerLifecycleObserverAdapter;
import com.example.william.my.core.banner.task.AutoLoopTask;
import com.example.william.my.core.banner.transformer.ScalePageTransformer;
import com.example.william.my.core.banner.util.BannerUtils;
import com.example.william.my.core.banner.util.ScrollSpeedManger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class Banner<T, BA extends BannerAdapter<T, ? extends RecyclerView.ViewHolder>> extends FrameLayout implements BannerLifecycleObserver {

    // ????????????
    private AutoLoopTask<T, BA> mLoopTask;
    // ????????????????????????
    private int mLoopTime = BannerConfig.LOOP_TIME;
    // ??????????????????
    private int mScrollTime = BannerConfig.SCROLL_TIME;
    // ??????????????????
    private boolean mIsAutoLoop = BannerConfig.IS_AUTO_LOOP;

    private BA mAdapter;
    private ViewPager2 mViewPager2;

    // ??????????????????
    private int mStartPosition = 1;
    // ?????????????????????????????????????????????
    private boolean mIsInfiniteLoop = BannerConfig.IS_INFINITE_LOOP;

    private BannerOnPageChangeCallback mPageChangeCallback;
    private BannerOnPageChangeListener mBannerOnPageChangeListener;

    private CompositePageTransformer mPageTransformer;

    //??????????????????
    private Paint mRoundPaint;
    private Paint mImagePaint;
    // banner?????????????????????????????????
    private float mBannerRadius = 0;
    // banner?????????????????????????????????????????????????????????????????????
    private boolean mRoundTopLeft, mRoundTopRight, mRoundBottomLeft, mRoundBottomRight;

    // ?????????????????????
    private boolean isIntercept = true;
    // ??????????????????
    private int mTouchSlop;
    // ???????????????????????????????????????????????????????????????
    private float mStartX, mStartY;
    // ??????viewpager2???????????????
    private boolean mIsViewPager2Drag;

    // Banner ??????
    private int mOrientation = Orientation.HORIZONTAL;

    // ?????????????????????
    private int normalColor = BannerConfig.INDICATOR_NORMAL_COLOR;
    private int selectedColor = BannerConfig.INDICATOR_SELECTED_COLOR;

    private int indicatorTextSize = BannerConfig.INDICATOR_TEXT_SIZE;

    private int normalWidth = BannerConfig.INDICATOR_NORMAL_WIDTH;
    private int selectedWidth = BannerConfig.INDICATOR_SELECTED_WIDTH;

    private int indicatorHeight = BannerConfig.INDICATOR_HEIGHT;
    private int indicatorRadius = BannerConfig.INDICATOR_RADIUS;

    private int indicatorGravity = BannerIndicatorConfig.Direction.CENTER;

    private int indicatorSpace;
    private int indicatorMargin;
    private int indicatorMarginLeft;
    private int indicatorMarginTop;
    private int indicatorMarginRight;
    private int indicatorMarginBottom;

    @IntDef({Orientation.HORIZONTAL, Orientation.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
        int HORIZONTAL = ViewPager2.ORIENTATION_HORIZONTAL;
        int VERTICAL = ViewPager2.ORIENTATION_VERTICAL;
    }

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Banner);
            mLoopTime = a.getInt(R.styleable.Banner_banner_loop_time, BannerConfig.LOOP_TIME);
            mIsAutoLoop = a.getBoolean(R.styleable.Banner_banner_auto_loop, BannerConfig.IS_AUTO_LOOP);
            mIsInfiniteLoop = a.getBoolean(R.styleable.Banner_banner_infinite_loop, BannerConfig.IS_INFINITE_LOOP);
            //
            mOrientation = a.getInt(R.styleable.Banner_banner_orientation, Orientation.HORIZONTAL);
            // ??????
            mBannerRadius = a.getDimensionPixelSize(R.styleable.Banner_banner_radius, 0);
            mRoundTopLeft = a.getBoolean(R.styleable.Banner_banner_round_top_left, false);
            mRoundTopRight = a.getBoolean(R.styleable.Banner_banner_round_top_right, false);
            mRoundBottomLeft = a.getBoolean(R.styleable.Banner_banner_round_bottom_left, false);
            mRoundBottomRight = a.getBoolean(R.styleable.Banner_banner_round_bottom_right, false);
            // ?????????
            normalColor = a.getColor(R.styleable.Banner_banner_indicator_normal_color, BannerConfig.INDICATOR_NORMAL_COLOR);
            selectedColor = a.getColor(R.styleable.Banner_banner_indicator_selected_color, BannerConfig.INDICATOR_SELECTED_COLOR);
            indicatorTextSize = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_text_size, BannerConfig.INDICATOR_TEXT_SIZE);
            normalWidth = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_normal_width, BannerConfig.INDICATOR_NORMAL_WIDTH);
            selectedWidth = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_selected_width, BannerConfig.INDICATOR_SELECTED_WIDTH);
            indicatorHeight = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_height, BannerConfig.INDICATOR_HEIGHT);
            indicatorRadius = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_radius, BannerConfig.INDICATOR_RADIUS);
            indicatorGravity = a.getInt(R.styleable.Banner_banner_indicator_gravity, BannerIndicatorConfig.Direction.CENTER);
            indicatorSpace = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_space, 0);
            indicatorMargin = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_margin, 0);
            indicatorMarginLeft = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginLeft, 0);
            indicatorMarginTop = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginTop, 0);
            indicatorMarginRight = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginRight, 0);
            indicatorMarginBottom = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginBottom, 0);
            a.recycle();
        }
    }

    private void initView(Context context) {
        mLoopTask = new AutoLoopTask<>(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() / 2;

        mViewPager2 = new ViewPager2(context);
        mViewPager2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mViewPager2.setOffscreenPageLimit(2);

        mPageChangeCallback = new BannerOnPageChangeCallback();
        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback);

        mPageTransformer = new CompositePageTransformer();
        mViewPager2.setPageTransformer(mPageTransformer);
        ScrollSpeedManger.reflectLayoutManager(this);
        addView(mViewPager2);

        mRoundPaint = new Paint();
        mRoundPaint.setColor(Color.WHITE);
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setStyle(Paint.Style.FILL);
        mRoundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mImagePaint = new Paint();
        mImagePaint.setXfermode(null);

        setOrientation(mOrientation);
    }

    private void setInfiniteLoop() {
        // ???????????????????????????????????????????????????
        if (!isInfiniteLoop()) {
            setAutoLoop(false);
        }
        setStartPosition(isInfiniteLoop() ? mStartPosition : 0);
    }

    private void initIndicatorAttr() {
        if (normalWidth > 0) {
            setIndicatorNormalWidth(normalWidth);
        }
        if (selectedWidth > 0) {
            setIndicatorSelectedWidth(selectedWidth);
        }
        if (indicatorTextSize > 0) {
            setIndicatorSelectedWidth(indicatorTextSize);
        }
        if (indicatorHeight > 0) {
            setIndicatorHeight(indicatorHeight);
        }
        if (indicatorRadius > 0) {
            setIndicatorRadius(indicatorRadius);
        }
        if (indicatorGravity != BannerIndicatorConfig.Direction.CENTER) {
            setIndicatorGravity(indicatorGravity);
        }
        if (indicatorSpace > 0) {
            setIndicatorSpace(indicatorSpace);
        }
        if (indicatorMargin != 0) {
            setIndicatorMargins(new BannerIndicatorMargins(indicatorMargin));
        } else if (indicatorMarginLeft != 0 || indicatorMarginTop != 0 || indicatorMarginRight != 0 || indicatorMarginBottom != 0) {
            setIndicatorMargins(new BannerIndicatorMargins(indicatorMarginLeft, indicatorMarginTop, indicatorMarginRight, indicatorMarginBottom));
        }
        setIndicatorNormalColor(normalColor);
        setIndicatorSelectedColor(selectedColor);
    }

    private void initIndicator() {
        if (getIndicator() == null || getAdapter() == null) {
            return;
        }
        if (getIndicator().getIndicatorConfig().isAttachToBanner()) {
            removeIndicator();
            addView(getIndicator().getIndicatorView());
        }
        initIndicatorAttr();
        setIndicatorPageChange();
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!getViewPager2().isUserInputEnabled() || !isIntercept) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);
                if (getViewPager2().getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    mIsViewPager2Drag = distanceX > mTouchSlop && distanceX > distanceY;
                } else {
                    mIsViewPager2Drag = distanceY > mTouchSlop && distanceY > distanceX;
                }
                getParent().requestDisallowInterceptTouchEvent(mIsViewPager2Drag);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mBannerRadius > 0) {
            canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), mImagePaint, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            //???????????????????????????
            if (!mRoundTopRight && !mRoundTopLeft && !mRoundBottomRight && !mRoundBottomLeft) {
                drawTopLeft(canvas);
                drawTopRight(canvas);
                drawBottomLeft(canvas);
                drawBottomRight(canvas);
                canvas.restore();
                return;
            }
            if (mRoundTopLeft) {
                drawTopLeft(canvas);
            }
            if (mRoundTopRight) {
                drawTopRight(canvas);
            }
            if (mRoundBottomLeft) {
                drawBottomLeft(canvas);
            }
            if (mRoundBottomRight) {
                drawBottomRight(canvas);
            }
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }

    private void drawTopLeft(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, mBannerRadius);
        path.lineTo(0, 0);
        path.lineTo(mBannerRadius, 0);
        path.arcTo(new RectF(0, 0, mBannerRadius * 2, mBannerRadius * 2), -90, -90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
    }

    private void drawTopRight(Canvas canvas) {
        int width = getWidth();
        Path path = new Path();
        path.moveTo(width - mBannerRadius, 0);
        path.lineTo(width, 0);
        path.lineTo(width, mBannerRadius);
        path.arcTo(new RectF(width - 2 * mBannerRadius, 0, width, mBannerRadius * 2), 0, -90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
    }

    private void drawBottomLeft(Canvas canvas) {
        int height = getHeight();
        Path path = new Path();
        path.moveTo(0, height - mBannerRadius);
        path.lineTo(0, height);
        path.lineTo(mBannerRadius, height);
        path.arcTo(new RectF(0, height - 2 * mBannerRadius, mBannerRadius * 2, height), 90, 90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
    }

    private void drawBottomRight(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        Path path = new Path();
        path.moveTo(width - mBannerRadius, height);
        path.lineTo(width, height);
        path.lineTo(width, height - mBannerRadius);
        path.arcTo(new RectF(width - 2 * mBannerRadius, height - 2 * mBannerRadius, width, height), 0, 90);
        path.close();
        canvas.drawPath(path, mRoundPaint);
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

    private class BannerOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

        private int mTempPosition;
        private boolean isScrolled;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), position, getRealCount());
            if (mBannerOnPageChangeListener != null && realPosition == getCurrentItem() - 1) {
                mBannerOnPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
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
                if (mBannerOnPageChangeListener != null) {
                    mBannerOnPageChangeListener.onPageSelected(realPosition);
                }
                if (getIndicator() != null) {
                    getIndicator().onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //???????????????,?????????????????????
            if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                isScrolled = true;
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                //???????????????????????????
                isScrolled = false;
                if (mTempPosition != -1 && mIsInfiniteLoop) {
                    if (mTempPosition == 0) {
                        setCurrentItem(getRealCount(), false);
                    } else if (mTempPosition == getItemCount() - 1) {
                        setCurrentItem(1, false);
                    }
                }
            }
            if (mBannerOnPageChangeListener != null) {
                mBannerOnPageChangeListener.onPageScrollStateChanged(state);
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

    private void setRecyclerViewPadding(int itemPadding) {
        setRecyclerViewPadding(itemPadding, itemPadding);
    }

    private void setRecyclerViewPadding(int itemPadding1, int itemPadding2) {
        RecyclerView recyclerView = (RecyclerView) getViewPager2().getChildAt(0);
        if (getViewPager2().getOrientation() == ViewPager2.ORIENTATION_VERTICAL) {
            recyclerView.setPadding(mViewPager2.getPaddingLeft(), itemPadding1, mViewPager2.getPaddingRight(), itemPadding2);
        } else {
            recyclerView.setPadding(itemPadding1, mViewPager2.getPaddingTop(), itemPadding2, mViewPager2.getPaddingBottom());
        }
        recyclerView.setClipToPadding(false);
    }

    /**
     * **********************************************************************
     * ------------------------ ????????????API ---------------------------------*
     * **********************************************************************
     */

    public AutoLoopTask<T, BA> getLoopTask() {
        return mLoopTask;
    }

    public int getLoopTime() {
        return mLoopTime;
    }

    public int getScrollTime() {
        return mScrollTime;
    }

    public boolean isAutoLoop() {
        return mIsAutoLoop;
    }

    public boolean isInfiniteLoop() {
        return mIsInfiniteLoop;
    }

    public BA getAdapter() {
        return mAdapter;
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    /**
     * ??????banner????????????
     */
    public int getItemCount() {
        if (getAdapter() != null) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    /**
     * ??????banner????????????
     */
    public int getRealCount() {
        if (getAdapter() != null) {
            return getAdapter().getRealCount();
        }
        return 0;
    }

    /**
     * ??????????????????
     */
    public int getCurrentItem() {
        return getViewPager2().getCurrentItem();
    }

    public BannerIndicatorConfig getIndicatorConfig() {
        if (getIndicator() != null) {
            return getIndicator().getIndicatorConfig();
        }
        return null;
    }

    //-----------------------------------------------------------------------------------------

    /**
     * ?????????????????????
     *
     * @param intercept
     * @return
     */
    public Banner<T, BA> setIntercept(boolean intercept) {
        isIntercept = intercept;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param enabled true ?????????false ??????
     */
    public Banner<T, BA> setUserInputEnabled(boolean enabled) {
        getViewPager2().setUserInputEnabled(enabled);
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param isAutoLoop ture ?????????false ?????????
     */
    public Banner<T, BA> setAutoLoop(boolean isAutoLoop) {
        this.mIsAutoLoop = isAutoLoop;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param loopTime ??????????????????
     */
    public Banner<T, BA> setLoopTime(int loopTime) {
        this.mLoopTime = loopTime;
        return this;
    }

    /**
     * ?????????????????????????????????
     */
    public Banner<T, BA> setScrollTime(int scrollTime) {
        this.mScrollTime = scrollTime;
        return this;
    }

    /**
     * ????????????????????????
     */
    public Banner<T, BA> setTouchSlop(int mTouchSlop) {
        this.mTouchSlop = mTouchSlop;
        return this;
    }

    /**
     * ????????????????????? (?????????setAdapter??????setData????????????????????????)
     */
    public Banner<T, BA> setStartPosition(int mStartPosition) {
        this.mStartPosition = mStartPosition;
        return this;
    }

    /**
     * ??????banner????????????
     */
    public Banner<T, BA> setAdapter(BA adapter) {
        setAdapter(adapter, true);
        return this;
    }

    /**
     * ??????banner????????????
     *
     * @param adapter
     * @param isInfiniteLoop ????????????????????????
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
     * ????????????banner?????????????????????????????????adapter?????????????????????,???????????????????????????????????????????????????
     *
     * @param data ?????????????????????null??????data??????????????????banner????????????????????????????????????UI??????
     */
    public Banner<T, BA> setData(List<T> data) {
        if (getAdapter() != null) {
            getAdapter().setData(data);
            setCurrentItem(mStartPosition, false);
            setIndicatorPageChange();
            start();
        }
        return this;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     *
     * @param position
     * @return
     */
    public Banner<T, BA> setCurrentItem(int position) {
        return setCurrentItem(position, true);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
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
     * ??????banner????????????
     *
     * @param orientation {@link Orientation}
     */
    public Banner<T, BA> setOrientation(@Orientation int orientation) {
        getViewPager2().setOrientation(orientation);
        return this;
    }

    /**
     * ??????????????????
     */
    public Banner<T, BA> setOnBannerListener(BannerOnBannerListener<T> listener) {
        if (getAdapter() != null) {
            getAdapter().setOnBannerListener(listener);
        }
        return this;
    }

    /**
     * ??????viewpager????????????
     * <p>
     * ???viewpager2???????????????{@link ViewPager2.OnPageChangeCallback}?????????????????????
     * ??????????????????????????????????????????viewpager?????????{@link ViewPager.OnPageChangeListener}??????
     * </p>
     */
    public Banner<T, BA> addOnPageChangeListener(BannerOnPageChangeListener pageListener) {
        this.mBannerOnPageChangeListener = pageListener;
        return this;
    }

    /**
     * ??????PageTransformer
     */
    public Banner<T, BA> setPageTransformer(ViewPager2.PageTransformer transformer) {
        getViewPager2().setPageTransformer(transformer);
        return this;
    }

    /**
     * ??????PageTransformer?????????????????????
     * {@link ViewPager2.PageTransformer}
     * ????????????????????????implementation "androidx.viewpager2:viewpager2:1.0.0"
     */
    public Banner<T, BA> addPageTransformer(ViewPager2.PageTransformer transformer) {
        mPageTransformer.addTransformer(transformer);
        return this;
    }

    public Banner<T, BA> removeTransformer(ViewPager2.PageTransformer transformer) {
        mPageTransformer.removeTransformer(transformer);
        return this;
    }

    /**
     * ??????banner??????
     * <p>
     * ?????????????????????????????????????????????????????????0??????
     *
     * @param radius ????????????
     */
    public Banner<T, BA> setBannerRound(float radius) {
        mBannerRadius = BannerUtils.dp2px(radius);
        return this;
    }

    /**
     * ??????banner??????(??????????????????????????????????????????????????????)????????????5.0??????
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Banner<T, BA> setBannerRound2(float radius) {
        BannerUtils.setBannerRound(this, radius);
        return this;
    }

    /**
     * ???banner??????????????????
     *
     * @param pagePadding item??????padding,??????dp
     * @param pageMargin  ????????????,??????dp
     */
    public Banner<T, BA> setBannerGallery(int pagePadding, int pageMargin) {
        return setBannerGallery(pagePadding, pageMargin, .85f);
    }

    /**
     * ???banner??????????????????
     *
     * @param pagePadding1 item????????????padding,??????dp
     * @param pagePadding2 item????????????padding,??????dp
     * @param pageMargin   ????????????,??????dp
     */
    public Banner<T, BA> setBannerGallery(int pagePadding1, int pagePadding2, int pageMargin) {
        return setBannerGallery(pagePadding1, pagePadding2, pageMargin, .85f);
    }

    /**
     * ???banner??????????????????
     *
     * @param pagePadding item???????????????padding,??????dp
     * @param pageMargin  ????????????,??????dp
     * @param scale       ??????[0-1],1???????????????
     */
    public Banner<T, BA> setBannerGallery(int pagePadding, int pageMargin, float scale) {
        return setBannerGallery(pagePadding, pagePadding, pageMargin, scale);
    }

    /**
     * ???banner??????????????????
     *
     * @param pagePadding1 item????????????padding,??????dp
     * @param pagePadding2 item????????????padding,??????dp
     * @param pageMargin   ????????????,??????dp
     * @param scale        ??????[0-1],1???????????????
     */
    public Banner<T, BA> setBannerGallery(int pagePadding1, int pagePadding2, int pageMargin, float scale) {
        if (pageMargin > 0) {
            addPageTransformer(new MarginPageTransformer(BannerUtils.dp2px(pageMargin)));
        }
        if (scale < 1 && scale > 0) {
            addPageTransformer(new ScalePageTransformer(scale));
        }
        setRecyclerViewPadding(pagePadding1 > 0 ? BannerUtils.dp2px(pagePadding1 + pageMargin) : 0, pagePadding2 > 0 ? BannerUtils.dp2px(pagePadding2 + pageMargin) : 0);
        return this;
    }

    /**
     * ????????????
     */
    public Banner<T, BA> start() {
        if (mIsAutoLoop) {
            stop();
            postDelayed(mLoopTask, mLoopTime);
        }
        return this;
    }

    /**
     * ????????????
     */
    public Banner<T, BA> stop() {
        if (mIsAutoLoop) {
            removeCallbacks(mLoopTask);
        }
        return this;
    }

    /**
     * ??????????????????
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
     * ------------------------ ????????????????????? --------------------------------*
     * **********************************************************************
     */

    private Indicator mIndicator;

    public Indicator getIndicator() {
        return mIndicator;
    }

    /**
     * ?????????????????????(?????????banner???)
     */
    public Banner<T, BA> setIndicator(Indicator indicator) {
        return setIndicator(indicator, true);
    }

    /**
     * ?????????????????????(?????????????????????????????????????????????attachToBanner???false)
     *
     * @param attachToBanner ???????????????????????????banner??????false ?????????????????????????????????????????????????????????
     *                       ??????????????????false??????????????? setIndicatorGravity()???setIndicatorMargins() ??????????????????
     *                       ???????????????????????????????????????????????????????????????????????????????????????????????????demo
     */
    public Banner<T, BA> setIndicator(Indicator indicator, boolean attachToBanner) {
        removeIndicator();
        indicator.getIndicatorConfig().setAttachToBanner(attachToBanner);
        this.mIndicator = indicator;
        initIndicator();
        return this;
    }

    public Banner<T, BA> setIndicatorPageChange() {
        if (getIndicator() != null) {
            int realPosition = BannerUtils.getRealPosition(isInfiniteLoop(), getCurrentItem(), getRealCount());
            getIndicator().onPageChanged(getRealCount(), realPosition);
        }
        return this;
    }

    public Banner<T, BA> removeIndicator() {
        if (getIndicator() != null) {
            removeView(getIndicator().getIndicatorView());
        }
        return this;
    }

    public Banner<T, BA> setIndicatorSelectedColor(@ColorInt int color) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setSelectedColor(color);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorNormalColor(@ColorInt int color) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setNormalColor(color);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorSelectedColorRes(@ColorRes int color) {
        setIndicatorSelectedColor(ContextCompat.getColor(getContext(), color));
        return this;
    }

    public Banner<T, BA> setIndicatorNormalColorRes(@ColorRes int color) {
        setIndicatorNormalColor(ContextCompat.getColor(getContext(), color));
        return this;
    }

    public Banner<T, BA> setIndicatorTextSize(int textSize) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setIndicatorTextSize(textSize);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorNormalWidth(int normalWidth) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setNormalWidth(normalWidth);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorSelectedWidth(int selectedWidth) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setSelectedWidth(selectedWidth);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorWidth(int normalWidth, int selectedWidth) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setNormalWidth(normalWidth);
            getIndicatorConfig().setSelectedWidth(selectedWidth);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorHeight(int indicatorHeight) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setHeight(indicatorHeight);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorRadius(int indicatorRadius) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setRadius(indicatorRadius);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorGravity(int gravity) {
        if (getIndicatorConfig() != null && getIndicatorConfig().isAttachToBanner()) {
            getIndicatorConfig().setGravity(gravity);
            getIndicator().getIndicatorView().postInvalidate();
        }
        return this;
    }

    public Banner<T, BA> setIndicatorSpace(int indicatorSpace) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setIndicatorSpace(indicatorSpace);
        }
        return this;
    }

    public Banner<T, BA> setIndicatorMargins(BannerIndicatorMargins margins) {
        if (getIndicatorConfig() != null && getIndicatorConfig().isAttachToBanner()) {
            getIndicatorConfig().setMargins(margins);
            getIndicator().getIndicatorView().requestLayout();
        }
        return this;
    }

    /**
     * **********************************************************************
     * ------------------------ ?????????????????? --------------------------------*
     * **********************************************************************
     */

    public Banner<T, BA> addBannerLifecycleObserver(LifecycleOwner owner) {
        if (owner != null) {
            owner.getLifecycle().addObserver(new BannerLifecycleObserverAdapter(owner, this));
        }
        return this;
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        start();
    }

    @Override
    public void onPause(LifecycleOwner owner) {
        stop();
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        destroy();
    }
}
