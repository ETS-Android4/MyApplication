package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.william.my.core.banner.util.BannerUtils;

public class CircleIndicator extends BaseIndicator {

    private final int mMaxRadius, mIndicatorSpace;
    private final int mSelectedRadius, mNormalRadius;
    private final int mSelectedColor, mNormalColor;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSelectedRadius = BannerUtils.dp2px(16);
        mNormalRadius = BannerUtils.dp2px(8);
        mIndicatorSpace = BannerUtils.dp2px(8);

        //考虑当 选中和默认 的大小不一样的情况
        mMaxRadius = Math.max(mSelectedRadius, mNormalRadius);

        mSelectedColor = Color.BLACK;
        mNormalColor = Color.WHITE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIndicatorSize <= 1) {
            return;
        }
        // 默认宽度*（总数-1）+ 选中宽度 + 间距 *（总数-1)
        int width = mNormalRadius * 2 * (mIndicatorSize - 1) + mSelectedRadius * 2 + mIndicatorSpace * (mIndicatorSize - 1);
        setMeasuredDimension(width, mMaxRadius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndicatorSize <= 1) {
            return;
        }
        float left = 0;
        for (int i = 0; i < mIndicatorSize; i++) {
            mPaint.setColor(mCurrentPosition == i ? mSelectedColor : mNormalColor);
            int radius = mCurrentPosition == i ? mSelectedRadius : mNormalRadius;
            canvas.drawCircle(left + radius, mMaxRadius, radius, mPaint);
            left += (mCurrentPosition == i ? mSelectedRadius * 2 : mNormalRadius * 2) + mIndicatorSpace;
        }
    }
}
