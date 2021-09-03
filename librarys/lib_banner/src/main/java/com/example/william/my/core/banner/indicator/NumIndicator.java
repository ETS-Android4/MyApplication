package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.william.my.core.banner.util.BannerUtils;

public class NumIndicator extends BaseIndicator {

    private final RectF mRectF;
    private final int mWidth, mHeight, mRadius;

    public NumIndicator(Context context) {
        this(context, null);
    }

    public NumIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setTextSize(BannerUtils.dp2px(16));
        mPaint.setTextAlign(Paint.Align.CENTER);

        mWidth = BannerUtils.getScreenWidth(context);
        mHeight = BannerUtils.dp2px(48);
        mRadius = BannerUtils.dp2px(8);

        mRectF = new RectF(0f, 0f, mWidth, mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIndicatorSize <= 1) {
            return;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndicatorSize <= 1) {
            return;
        }
        mPaint.setColor(Color.BLACK);
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(BannerUtils.dp2px(14f));
        canvas.drawText(mCurrentPosition + 1 + "/" + mIndicatorSize, (float) mWidth / 2, (float) (mHeight * 0.6), mPaint);
    }
}
