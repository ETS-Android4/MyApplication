package com.example.william.my.core.imageloader.glide;

import android.app.Activity;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.william.my.core.imageloader.corner.CornerType;
import com.example.william.my.core.imageloader.glide.module.GlideApp;
import com.example.william.my.core.imageloader.glide.transformation.RadiusTransformation;
import com.example.william.my.core.imageloader.loader.ILoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * https://github.com/bumptech/glide/
 * <p>
 * FitCenter：不剪裁，不填充ImageVIew (默认)
 * CenterCrop：剪裁，完全填充ImageVIew
 */
public class GlideLoader implements ILoader {

    @Override
    public void load(FragmentActivity activity, String url, ImageView imageView) {
        loadActivity(activity, url, imageView, new RequestOptions());
    }

    @Override
    public void loadCircle(FragmentActivity activity, String url, ImageView imageView) {
        loadCircleActivity(activity, url, imageView);
    }

    @Override
    public void loadRadius(FragmentActivity activity, String url, ImageView imageView, float radius) {
        loadRadiusActivity(activity, url, imageView, radius, CornerType.ALL);
    }

    @Override
    public void loadRadius(FragmentActivity activity, String url, ImageView imageView, float radius, CornerType type) {
        loadRadiusActivity(activity, url, imageView, radius, type);
    }

    private void loadActivity(Activity activity, String url, ImageView imageView, RequestOptions options) {
        GlideApp.with(activity)
                .load(url)
                .fitCenter()
                .apply(options)
                .into(imageView);
    }

    private void loadCircleActivity(Activity activity, String url, ImageView imageView) {
        RequestOptions circleOptions = new RequestOptions().circleCrop();
        loadActivity(activity, url, imageView, circleOptions);
    }

    private void loadRadiusActivity(Activity activity, String url, ImageView imageView, float radius, CornerType type) {
        RequestOptions radiusOptions = new RequestOptions().transform(new RadiusTransformation(radius, type));
        loadActivity(activity, url, imageView, radiusOptions);
    }

    private void loadBlurActivity() {

    }

    private void loadGifActivity(Activity activity, String url, ImageView imageView, int count) {
        GlideApp.with(activity)
                .asGif()
                .load(url)
                .addListener(new RequestListener<GifDrawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(count);
                        return false;
                    }
                })
                .into(imageView);
    }

    private void loadGifActivity(Activity activity, String url, ImageView imageView, final GifListener gifListener) {
        GlideApp.with(activity)
                .asGif()
                .load(url)
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            Field gifStateField = GifDrawable.class.getDeclaredField("state");
                            gifStateField.setAccessible(true);
                            Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                            Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                            gifFrameLoaderField.setAccessible(true);
                            Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                            Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                            gifDecoderField.setAccessible(true);
                            Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                            Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                            Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                            getDelayMethod.setAccessible(true);
                            //设置只播放一次
                            resource.setLoopCount(1);
                            //获得总帧数
                            int count = resource.getFrameCount();
                            int delay = 0;
                            for (int i = 0; i < count; i++) {
                                //计算每一帧所需要的时间进行累加
                                delay += (int) getDelayMethod.invoke(gifDecoder, i);
                            }
                            imageView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (gifListener != null) {
                                        gifListener.gifPlayComplete();
                                    }
                                }
                            }, delay);
                        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * Gif播放完毕回调
     */
    public interface GifListener {
        void gifPlayComplete();
    }
}
