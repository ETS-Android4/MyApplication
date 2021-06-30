package com.example.william.my.core.imageloader.glide.transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.example.william.my.core.imageloader.corner.CornerType;

import java.security.MessageDigest;

/**
 * glide 定制圆角
 */
public class RadiusTransformation extends BitmapTransformation {


    private final float mRadius;
    private final float mDiameter;
    private final CornerType mCornerType;

    public RadiusTransformation(int radius) {
        this(radius, CornerType.ALL);
    }

    public RadiusTransformation(float radius, CornerType cornerType) {
        this.mRadius = radius;
        this.mDiameter = 2 * mRadius;
        this.mCornerType = cornerType;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return crop(pool, bitmap);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    private Bitmap crop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        Bitmap bitmap = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, source.getWidth(), source.getHeight());
        return bitmap;
    }

    /**
     * 将图片分成三部分绘制
     */
    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        switch (mCornerType) {
            case TOP_LEFT:
                drawLeftTopCorner(canvas, paint, width, height);
                break;
            case TOP_RIGHT:
                drawRightTopCorner(canvas, paint, width, height);
                break;
            case BOTTOM_LEFT:
                drawLeftBottomCorner(canvas, paint, width, height);
                break;
            case BOTTOM_RIGHT:
                drawRightBottomCorner(canvas, paint, width, height);
                break;
            case TOP:
                drawTopCorner(canvas, paint, width, height);
                break;
            case BOTTOM:
                drawBottomCorner(canvas, paint, width, height);
                break;
            case LEFT:
                drawLeftCorner(canvas, paint, width, height);
                break;
            case RIGHT:
                drawRightCorner(canvas, paint, width, height);
                break;
            case ALL:
            default:
                RectF rect = new RectF(0, 0, width, height);
                canvas.drawRoundRect(rect, mRadius, mRadius, paint);
                break;
        }
    }

    /**
     * 画左上角
     */
    private void drawLeftTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
    }

    /**
     * 画左下角
     */
    private void drawLeftBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右上角
     */
    private void drawRightTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画右下角
     */
    private void drawRightBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(0, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画左圆角
     */
    private void drawLeftCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height - mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右圆角
     */
    private void drawRightCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height - mRadius), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画上圆角
     */
    private void drawTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, mRadius, width, height), paint);
        canvas.drawRect(new RectF(mRadius, 0, width - mRadius, mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画下圆角
     */
    private void drawBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }
}
