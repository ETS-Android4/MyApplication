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

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/**
 * glide 定制圆角
 */
public class RadiusTransformation extends BitmapTransformation {

    public enum CornerType {
        /**
         * 所有角
         */
        ALL,
        /**
         * 左上，左下，右上，右下
         */
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM,
        /**
         * 左侧，右侧，下侧，上侧
         */
        LEFT, RIGHT, BOTTOM, TOP,
    }

    private final int mRadius;
    private final CornerType mCornerType;

    public RadiusTransformation(int radius) {
        this(radius, CornerType.ALL);
    }

    public RadiusTransformation(int radius, CornerType cornerType) {
        this.mRadius = radius;
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

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        switch (mCornerType) {
            case LEFT_TOP:
                drawLeftTopCorner(canvas, paint, width, height);
                break;
            case LEFT_BOTTOM:
                drawLeftBottomCorner(canvas, paint, width, height);
                break;
            case RIGHT_TOP:
                drawRightTopCorner(canvas, paint, width, height);
                break;
            case RIGHT_BOTTOM:
                drawRightBottomCorner(canvas, paint, width, height);
                break;
            case LEFT:
                drawLeftCorner(canvas, paint, width, height);
                break;
            case RIGHT:
                drawRightCorner(canvas, paint, width, height);
                break;
            case BOTTOM:
                drawBottomCorner(canvas, paint, width, height);
                break;
            case TOP:
                drawTopCorner(canvas, paint, width, height);
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

    }

    /**
     * 画左下角
     */
    private void drawLeftBottomCorner(@NotNull Canvas canvas, Paint paint, float width, float height) {

    }

    /**
     * 画右上角
     */
    private void drawRightTopCorner(@NotNull Canvas canvas, Paint paint, float width, float height) {

    }

    /**
     * 画右下角
     */
    private void drawRightBottomCorner(@NotNull Canvas canvas, Paint paint, float width, float height) {

    }

    /**
     * 画左圆角
     */
    private void drawLeftCorner(@NotNull Canvas canvas, Paint paint, float width, float height) {

    }

    /**
     * 画右圆角
     */
    private void drawRightCorner(@NotNull Canvas canvas, Paint paint, float width, float height) {

    }

    /**
     * 画上圆角
     */
    private void drawTopCorner(@NotNull Canvas canvas, Paint paint, float width, float height) {

    }

    /**
     * 画下圆角
     */
    private void drawBottomCorner(@NotNull Canvas canvas, Paint paint, float width, float height) {

    }
}
