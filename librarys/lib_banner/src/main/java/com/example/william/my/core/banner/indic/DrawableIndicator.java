package com.example.william.my.core.banner.indic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;

import com.example.william.my.core.banner.R;
import com.example.william.my.core.banner.util.BannerUtils;

public class DrawableIndicator extends BaseIndicator {

    private Bitmap normalBitmap, selectedBitmap;
    private int mIndicatorSpace, mIndicatorMargin;

    public DrawableIndicator(Context context, @DrawableRes int normalResId, @DrawableRes int selectedResId) {
        super(context);
        normalBitmap = BitmapFactory.decodeResource(getResources(), normalResId);
        selectedBitmap = BitmapFactory.decodeResource(getResources(), selectedResId);
    }

    public DrawableIndicator(Context context) {
        this(context, null);
    }

    public DrawableIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableIndicator);
        if (a != null) {
            BitmapDrawable normal = (BitmapDrawable) a.getDrawable(R.styleable.DrawableIndicator_normal_drawable);
            BitmapDrawable selected = (BitmapDrawable) a.getDrawable(R.styleable.DrawableIndicator_selected_drawable);
            normalBitmap = normal.getBitmap();
            selectedBitmap = selected.getBitmap();
        }
        mIndicatorSpace = BannerUtils.dp2px(8);
        mIndicatorMargin = BannerUtils.dp2px(8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIndicatorSize <= 1) {
            return;
        }

        // 默认宽度*（总数-1）+ 选中宽度 + 间距 *（总数-1) + 边距 * 2
        int width = normalBitmap.getWidth() * (mIndicatorSize - 1) + selectedBitmap.getWidth() + mIndicatorSpace * (mIndicatorSize - 1) + mIndicatorMargin * 2;
        setMeasuredDimension(width, Math.max(normalBitmap.getHeight(), selectedBitmap.getHeight()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndicatorSize <= 1 || normalBitmap == null || selectedBitmap == null) {
            return;
        }

        float left = mIndicatorMargin;
        for (int i = 0; i < mIndicatorSize; i++) {
            canvas.drawBitmap(mCurrentPosition == i ? selectedBitmap : normalBitmap, left, 0, mPaint);
            left += normalBitmap.getWidth() + mIndicatorSpace;
        }
    }
}
