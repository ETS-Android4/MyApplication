package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

public class NumIndicator extends BaseIndicator {

    private final float width;
    private final float height;

    private final RectF mRectF;
    private final Paint.FontMetrics mFontMetrics;

    public NumIndicator(Context context) {
        this(context, null);
    }

    public NumIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRectF = new RectF();

        mPaint.setTextSize(mConfig.getIndicatorTextSize());
        mPaint.setTextAlign(Paint.Align.CENTER);

        mFontMetrics = mPaint.getFontMetrics();

        width = mPaint.measureText(mConfig.getIndicatorTextSize() + "/" + mConfig.getIndicatorTextSize());
        height = (int) (mFontMetrics.bottom - mFontMetrics.top) + mConfig.getIndicatorSpace() * 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = mConfig.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = mConfig.getIndicatorSize();
        if (count <= 1) {
            return;
        }

        mRectF.set(0, 0, width, height);
        mPaint.setColor(mConfig.getNormalColor());
        canvas.drawRoundRect(mRectF, mConfig.getRadius(), mConfig.getRadius(), mPaint);

        mPaint.setColor(Color.WHITE);
        String text = mConfig.getCurrentPosition() + 1 + "/" + count;

        // 计算baseline
        float baseline = mRectF.centerY() + ((mFontMetrics.bottom - mFontMetrics.top) / 2 - mFontMetrics.bottom);
        canvas.drawText(text, mRectF.centerX(), baseline, mPaint);
    }
}
