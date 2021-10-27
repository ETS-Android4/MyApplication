package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class CircleIndicator extends BaseIndicator {

    private final int mMaxRadius;
    private final int mNormalRadius, mSelectedRadius;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mNormalRadius = mConfig.getNormalWidth() / 2;
        mSelectedRadius = mConfig.getSelectedWidth() / 2;
        //考虑当 选中和默认 的大小不一样的情况
        mMaxRadius = Math.max(mSelectedRadius, mNormalRadius);
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
        setMeasuredDimension(width, Math.max(mConfig.getNormalWidth(), mConfig.getSelectedWidth()));
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
            float radius = mConfig.getCurrentPosition() == i ? mSelectedRadius : mNormalRadius;
            canvas.drawCircle(left + radius, mMaxRadius, radius, mPaint);
            int width = mConfig.getCurrentPosition() == i ? mConfig.getSelectedWidth() : mConfig.getNormalWidth();
            left += width + mConfig.getIndicatorSpace();
        }
    }
}
