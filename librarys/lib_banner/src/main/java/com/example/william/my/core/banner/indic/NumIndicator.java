package com.example.william.my.core.banner.indic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.william.my.core.banner.util.BannerUtils;

public class NumIndicator extends BaseIndicator {

    //private final RectF mRectF;
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

        mWidth = BannerUtils.dp2px(60);
        mHeight = BannerUtils.dp2px(48);
        mRadius = BannerUtils.dp2px(4);

        //mRectF = new RectF(0f, 0f, mWidth, mHeight);
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
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(BannerUtils.dp2px(14f));
        String text = mCurrentPosition + 1 + "/" + mIndicatorSize;
        canvas.drawText(text, (float) mWidth / 2, (float) (mHeight * 0.6), mPaint);

        //mPaint.setColor(Color.BLACK);
        //canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }
}
