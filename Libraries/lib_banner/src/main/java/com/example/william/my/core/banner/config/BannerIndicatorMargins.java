package com.example.william.my.core.banner.config;

public class BannerIndicatorMargins {

    public int mLeftMargin;
    public int mTopMargin;
    public int mRightMargin;
    public int mBottomMargin;

    public BannerIndicatorMargins(int marginSize) {
        this(marginSize, marginSize, marginSize, marginSize);
    }

    public BannerIndicatorMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.mLeftMargin = leftMargin;
        this.mTopMargin = topMargin;
        this.mRightMargin = rightMargin;
        this.mBottomMargin = bottomMargin;
    }
}