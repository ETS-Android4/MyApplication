package com.example.william.my.core.banner.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;

import com.example.william.my.core.banner.R;

public class DrawableIndicator extends BaseIndicator {

    private Bitmap mNormalBitmap;
    private Bitmap mSelectedBitmap;

    public DrawableIndicator(Context context, @DrawableRes int normalResId, @DrawableRes int selectedResId) {
        super(context);
        mNormalBitmap = BitmapFactory.decodeResource(getResources(), normalResId);
        mSelectedBitmap = BitmapFactory.decodeResource(getResources(), selectedResId);
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
            mNormalBitmap = normal.getBitmap();
            mSelectedBitmap = selected.getBitmap();
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = mConfig.getIndicatorSize();
        if (count <= 1) {
            return;
        }

        // 默认宽度 *（总数-1）+ 选中宽度 + 间距 *（总数-1)
        int width = mSelectedBitmap.getWidth() * (count - 1) + mSelectedBitmap.getWidth() + mConfig.getIndicatorSpace() * (count - 1);
        setMeasuredDimension(width, Math.max(mNormalBitmap.getHeight(), mSelectedBitmap.getHeight()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = mConfig.getIndicatorSize();
        if (count <= 1 || mNormalBitmap == null || mSelectedBitmap == null) {
            return;
        }

        float left = 0;
        for (int i = 0; i < count; i++) {
            canvas.drawBitmap(mConfig.getCurrentPosition() == i ? mSelectedBitmap : mNormalBitmap, left, 0, mPaint);
            left += mNormalBitmap.getWidth() + mConfig.getIndicatorSpace();
        }
    }
}
