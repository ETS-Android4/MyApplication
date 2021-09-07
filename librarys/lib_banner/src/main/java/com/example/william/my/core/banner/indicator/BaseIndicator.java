package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.william.my.core.banner.config.BannerIndicatorConfig;

public class BaseIndicator extends View implements Indicator {

    protected Paint mPaint;
    protected float mOffset;

    protected BannerIndicatorConfig mConfig;

    public BaseIndicator(Context context) {
        this(context, null);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mConfig = new BannerIndicatorConfig();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setColor(mConfig.getNormalColor());
    }

    @NonNull
    @Override
    public View getIndicatorView() {
        if (mConfig.isAttachToBanner()) {
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            switch (mConfig.getGravity()) {
                case Gravity.LEFT:
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
                    break;
                case Gravity.CENTER:
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                    break;
                case Gravity.RIGHT:
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                    break;
            }
            layoutParams.leftMargin = mConfig.getMargins().mLeftMargin;
            layoutParams.rightMargin = mConfig.getMargins().mRightMargin;
            layoutParams.topMargin = mConfig.getMargins().mTopMargin;
            layoutParams.bottomMargin = mConfig.getMargins().mBottomMargin;
            setLayoutParams(layoutParams);
        }
        return this;
    }

    @Override
    public BannerIndicatorConfig getIndicatorConfig() {
        return mConfig;
    }

    @Override
    public void onPageChanged(int count, int currentPosition) {
        mConfig.setIndicatorSize(count);
        mConfig.setCurrentPosition(currentPosition);
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        mConfig.setCurrentPosition(position);
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
