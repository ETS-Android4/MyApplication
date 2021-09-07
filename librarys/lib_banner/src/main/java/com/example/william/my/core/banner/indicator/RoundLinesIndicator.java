package com.example.william.my.core.banner.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class RoundLinesIndicator extends BaseIndicator {

    public RoundLinesIndicator(Context context) {
        this(context, null);
    }

    public RoundLinesIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundLinesIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = mConfig.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        setMeasuredDimension((int) (mConfig.getSelectedWidth() * count), mConfig.getHeight());
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = mConfig.getIndicatorSize();
        if (count <= 1) {
            return;
        }

        mPaint.setColor(mConfig.getNormalColor());
        RectF oval = new RectF(0, 0, getWidth(), mConfig.getHeight());
        canvas.drawRoundRect(oval, mConfig.getRadius(), mConfig.getRadius(), mPaint);

        mPaint.setColor(mConfig.getSelectedColor());
        int left = mConfig.getCurrentPosition() * mConfig.getSelectedWidth();
        RectF rectF = new RectF(left, 0, left + mConfig.getSelectedWidth(), mConfig.getHeight());
        canvas.drawRoundRect(rectF, mConfig.getRadius(), mConfig.getRadius(), mPaint);
    }
}
