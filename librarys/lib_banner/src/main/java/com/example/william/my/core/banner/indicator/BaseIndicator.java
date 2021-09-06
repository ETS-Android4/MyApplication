package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.william.my.core.banner.config.BannerIndicatorConfig;

public class BaseIndicator extends View implements Indicator {

    protected BannerIndicatorConfig config;
    protected Paint mPaint;
    protected float offset;

    public BaseIndicator(Context context) {
        this(context, null);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        config = new BannerIndicatorConfig();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setColor(config.getNormalColor());
    }

    @NonNull
    @Override
    public View getIndicatorView() {
        if (config.isAttachToBanner()) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            switch (config.getGravity()) {
                case BannerIndicatorConfig.Direction.LEFT:
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
                    break;
                case BannerIndicatorConfig.Direction.CENTER:
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                    break;
                case BannerIndicatorConfig.Direction.RIGHT:
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                    break;
            }
            layoutParams.leftMargin = config.getMargins().leftMargin;
            layoutParams.rightMargin = config.getMargins().rightMargin;
            layoutParams.topMargin = config.getMargins().topMargin;
            layoutParams.bottomMargin = config.getMargins().bottomMargin;
            setLayoutParams(layoutParams);
        }
        return this;
    }

    @Override
    public BannerIndicatorConfig getIndicatorConfig() {
        return config;
    }

    @Override
    public void onPageChanged(int count, int currentPosition) {
        config.setIndicatorSize(count);
        config.setCurrentPosition(currentPosition);
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        offset = positionOffset;
        invalidate();

    }

    @Override
    public void onPageSelected(int position) {
        config.setCurrentPosition(position);
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
