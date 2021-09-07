package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RectangleIndicator extends BaseIndicator {

    private final RectF mRectF;

    public RectangleIndicator(Context context) {
        this(context, null);
    }

    public RectangleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = mConfig.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        // 默认宽度 *（总数-1）+ 选中宽度 + 间距 *（总数-1)
        int width = mConfig.getNormalWidth() * (count - 1) + mConfig.getSelectedWidth() + mConfig.getIndicatorSpace() * (count - 1);
        setMeasuredDimension(width, mConfig.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = mConfig.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        float left = 0;
        for (int i = 0; i < count; i++) {
            mPaint.setColor(mConfig.getCurrentPosition() == i ? mConfig.getSelectedColor() : mConfig.getNormalColor());
            int width = mConfig.getCurrentPosition() == i ? mConfig.getSelectedWidth() : mConfig.getNormalWidth();
            mRectF.set(left, 0, left + width, mConfig.getHeight());
            left += width + mConfig.getIndicatorSpace();
            canvas.drawRoundRect(mRectF, mConfig.getRadius(), mConfig.getRadius(), mPaint);
        }
    }
}
