package com.example.william.my.core.imageloader;

import com.example.william.my.core.imageloader.glide.GlideLoader;
import com.example.william.my.core.imageloader.loader.ILoader;

public class ImageLoader {

    private static ILoader sImageLoader;

    public static ILoader getInstance() {
        if (sImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (sImageLoader == null) {
                    //sImageLoader = new GlideLoader();
                }
            }
        }
        return sImageLoader;
    }
}
