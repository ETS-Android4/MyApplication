package com.example.william.my.core.banner.indic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.william.my.core.banner.util.BannerUtils;

public class RoundLinesIndicator extends BaseIndicator {

    private final int mRadius;

    private final float mSelectedWidth, mHeight;

    public RoundLinesIndicator(Context context) {
        this(context, null);
    }

    public RoundLinesIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundLinesIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setStyle(Paint.Style.FILL);

        mRadius = BannerUtils.dp2px(4);
        mSelectedWidth = BannerUtils.dp2px(16);
        mHeight = BannerUtils.dp2px(8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIndicatorSize <= 1) {
            return;
        }
        setMeasuredDimension((int) mSelectedWidth * mIndicatorSize, (int) mHeight);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndicatorSize <= 1) {
            return;
        }

        mPaint.setColor(mNormalColor);
        RectF oval = new RectF(0, 0, getWidth(), mHeight);
        canvas.drawRoundRect(oval, mRadius, mRadius, mPaint);

        mPaint.setColor(mSelectedColor);
        float left = mCurrentPosition * mSelectedWidth;
        RectF rectF = new RectF(left, 0, left + mSelectedWidth, mHeight);
        canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
    }
}
