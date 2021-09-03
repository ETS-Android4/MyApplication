package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.william.my.core.banner.util.BannerUtils;

public class NumIndicator extends BaseIndicator {

    private final float width;
    private final float height;
    private final float radius;

    private final RectF rectF;

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

        width = BannerUtils.getScreenWidth(context);
        height = BannerUtils.dp2px(48);

        radius = BannerUtils.dp2px(8);

        rectF = new RectF(0f, 0f, width, height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (indicatorSize <= 1) {
            return;
        }
        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indicatorSize <= 1) {
            return;
        }
        mPaint.setColor(Color.BLACK);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawText(currentPosition + 1 + "/" + indicatorSize, width / 2, (float) (height * 0.6), mPaint);
    }
}
