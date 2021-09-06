package com.example.william.my.core.banner.indic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaseIndicator extends View implements Indicator {

    protected Paint mPaint;

    protected Context mContext;
    protected int mIndicatorSize;
    protected int mCurrentPosition;

    protected final int mSelectedColor, mNormalColor, mBackgroundColor;

    public BaseIndicator(Context context) {
        this(context, null);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);

        mSelectedColor = Color.BLACK;
        mNormalColor = Color.WHITE;
        mBackgroundColor = Color.BLACK;
    }

    @NonNull
    @Override
    public View getIndicatorView() {
        return this;
    }

    @Override
    public void onPageChanged(int count, int currentPosition) {
        this.mIndicatorSize = count;
        this.mCurrentPosition = currentPosition;
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mCurrentPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
