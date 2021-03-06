package com.example.william.my.core.banner.adapter;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.william.my.core.banner.holder.BannerImageHolder;

import java.util.List;

/**
 * 默认实现的图片适配器，图片加载需要自己实现
 */
public abstract class BannerImageAdapter<T> extends BannerAdapter<T, BannerImageHolder> {

    public BannerImageAdapter(List<T> mData) {
        super(mData);
    }

    @Override
    public BannerImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerImageHolder(imageView);
    }
}
