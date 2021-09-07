package com.example.william.my.core.banner.config;

import android.view.Gravity;

import androidx.annotation.ColorInt;

public class BannerIndicatorConfig {

    private int mIndicatorSize;
    private int mCurrentPosition;
    private int mGravity = Gravity.CENTER;

    @ColorInt
    private int mNormalColor = BannerConfig.INDICATOR_NORMAL_COLOR;
    @ColorInt
    private int mSelectedColor = BannerConfig.INDICATOR_SELECTED_COLOR;

    private int mIndicatorTextSize = BannerConfig.INDICATOR_TEXT_SIZE;

    private int mNormalWidth = BannerConfig.INDICATOR_NORMAL_WIDTH;
    private int mSelectedWidth = BannerConfig.INDICATOR_SELECTED_WIDTH;

    private int mIndicatorHeight = BannerConfig.INDICATOR_HEIGHT;
    private int mIndicatorRadius = BannerConfig.INDICATOR_RADIUS;


    private BannerIndicatorMargins mIndicatorMargins;
    private int mIndicatorSpace = BannerConfig.INDICATOR_SPACE;

    //是将指示器添加到banner上
    private boolean attachToBanner = true;

    public int getIndicatorSize() {
        return mIndicatorSize;
    }

    public BannerIndicatorConfig setIndicatorSize(int mIndicatorSize) {
        this.mIndicatorSize = mIndicatorSize;
        return this;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public BannerIndicatorConfig setCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
        return this;
    }

    public int getGravity() {
        return mGravity;
    }

    public BannerIndicatorConfig setGravity(int mGravity) {
        this.mGravity = mGravity;
        return this;
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public BannerIndicatorConfig setNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
        return this;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public BannerIndicatorConfig setSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
        return this;
    }

    public int getIndicatorTextSize() {
        return mIndicatorTextSize;
    }

    public void setIndicatorTextSize(int mIndicatorTextSize) {
        this.mIndicatorTextSize = mIndicatorTextSize;
    }

    public int getNormalWidth() {
        return mNormalWidth;
    }

    public BannerIndicatorConfig setNormalWidth(int mNormalWidth) {
        this.mNormalWidth = mNormalWidth;
        return this;
    }

    public int getSelectedWidth() {
        return mSelectedWidth;
    }

    public BannerIndicatorConfig setSelectedWidth(int mSelectedWidth) {
        this.mSelectedWidth = mSelectedWidth;
        return this;
    }

    public int getHeight() {
        return mIndicatorHeight;
    }

    public BannerIndicatorConfig setHeight(int mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
        return this;
    }

    public int getRadius() {
        return mIndicatorRadius;
    }

    public BannerIndicatorConfig setRadius(int mRadius) {
        this.mIndicatorRadius = mRadius;
        return this;
    }

    public BannerIndicatorMargins getMargins() {
        if (mIndicatorMargins == null) {
            setMargins(new BannerIndicatorMargins(BannerConfig.INDICATOR_MARGIN));
        }
        return mIndicatorMargins;
    }

    public BannerIndicatorConfig setMargins(BannerIndicatorMargins mIndicatorMargins) {
        this.mIndicatorMargins = mIndicatorMargins;
        return this;
    }

    public int getIndicatorSpace() {
        return mIndicatorSpace;
    }

    public BannerIndicatorConfig setIndicatorSpace(int mIndicatorSpace) {
        this.mIndicatorSpace = mIndicatorSpace;
        return this;
    }

    public boolean isAttachToBanner() {
        return attachToBanner;
    }

    public BannerIndicatorConfig setAttachToBanner(boolean attachToBanner) {
        this.attachToBanner = attachToBanner;
        return this;
    }
}
