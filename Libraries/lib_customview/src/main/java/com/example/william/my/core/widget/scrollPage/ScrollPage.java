package com.example.william.my.core.widget.scrollPage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.william.my.core.widget.R;

/**
 * ScrollView分页
 */
public class ScrollPage extends FrameLayout {

    private final ScrollPageLayout mScrollPageLayout;
    private final LinearLayout mScrollPageContent;

    private final ScrollPageOne mScrollPageOne;
    private final ScrollPageTwo mScrollPageTwo;

    public ScrollPage(@NonNull Context context) {
        this(context, null);
    }

    public ScrollPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPage(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScrollPageLayout = new ScrollPageLayout(context);
        mScrollPageLayout.setFillViewport(true);

        mScrollPageContent = new LinearLayout(context);
        mScrollPageContent.setOrientation(LinearLayout.VERTICAL);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollPage);

        mScrollPageOne = new ScrollPageOne(context);
        LayoutInflater.from(context).inflate(a.getResourceId(R.styleable.ScrollPage_pageOne, 0), mScrollPageOne, true);

        mScrollPageTwo = new ScrollPageTwo(context);
        LayoutInflater.from(context).inflate(a.getResourceId(R.styleable.ScrollPage_pageTwo, 0), mScrollPageTwo, true);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        mScrollPageContent.removeAllViews();
        mScrollPageContent.addView(mScrollPageOne, LayoutParams.MATCH_PARENT, screenHeight);
        mScrollPageContent.addView(mScrollPageTwo, LayoutParams.MATCH_PARENT, screenHeight);

        mScrollPageLayout.removeAllViews();
        mScrollPageLayout.addView(mScrollPageContent, LayoutParams.MATCH_PARENT, screenHeight);

        removeAllViews();
        addView(mScrollPageLayout, LayoutParams.MATCH_PARENT, screenHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static class ScrollPageLayout extends ScrollView {

        private int screenHeight;
        private boolean isPageOne = true;

        public ScrollPageLayout(Context context) {
            this(context, null);
        }

        public ScrollPageLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ScrollPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            screenHeight = MeasureSpec.getSize(heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (changed) {
                this.scrollTo(0, 0);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    performClick();
                    break;
                case MotionEvent.ACTION_UP:
                    int scrollY = getScrollY();
                    //滑动多少距离
                    int criteria = screenHeight / 6;
                    if (isPageOne) {
                        if (scrollY <= criteria) {
                            this.smoothScrollTo(0, 0);
                        } else {
                            this.smoothScrollTo(0, screenHeight);
                            this.setFocusable(false);
                            isPageOne = false;
                        }
                    } else {
                        int scrollPadding = screenHeight - scrollY;
                        if (scrollPadding >= criteria) {
                            this.smoothScrollTo(0, 0);
                            isPageOne = true;
                        } else {
                            this.smoothScrollTo(0, screenHeight);
                        }
                    }
                    return true;
            }
            return super.onTouchEvent(ev);
        }

        @Override
        public boolean performClick() {
            return super.performClick();
        }
    }
}
