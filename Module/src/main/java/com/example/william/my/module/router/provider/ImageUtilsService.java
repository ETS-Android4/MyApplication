package com.example.william.my.module.router.provider;

import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.io.File;

public interface ImageUtilsService extends IProvider {

    boolean save(Bitmap bitmap, String filePath, Bitmap.CompressFormat format);

    boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat format);
}
