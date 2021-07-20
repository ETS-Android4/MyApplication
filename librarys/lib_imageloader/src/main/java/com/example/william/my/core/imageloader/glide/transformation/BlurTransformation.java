package com.example.william.my.core.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * glide 模糊变换
 */
public class BlurTransformation extends BitmapTransformation {

    private static final float MAX_RADIUS = 25;//DEFAULT_RADIUS
    private static final float DEFAULT_DOWN_SAMPLING = 1;//DEFAULT_SAMPLING

    private final float mRadius;
    private final float mSampling;
    private final Context mContext;

    public BlurTransformation(Context context) {
        this(context, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, float radius) {
        this(context, radius, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, float radius, float sampling) {
        this.mContext = context;
        this.mRadius = radius;
        this.mSampling = sampling;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);

        int scaledWidth = (int) (toTransform.getWidth() / mSampling);
        int scaledHeight = (int) (toTransform.getHeight() / mSampling);

        Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / mSampling, 1 / mSampling);
        canvas.drawBitmap(toTransform, 0, 0, paint);

        try {
            RenderScript rs = RenderScript.create(mContext);
            Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            blur.setRadius(mRadius);
            blur.setInput(input);
            blur.forEach(output);
            output.copyTo(bitmap);
            rs.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
