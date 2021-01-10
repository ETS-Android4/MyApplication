package com.example.william.my.util.router;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ImageUtils;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.ImageUtilsService;

import java.io.File;

@Route(path = ARouterPath.Service.ImageUtilsService)
public class ImageUtilsServiceImpl implements ImageUtilsService {

    @Override
    public boolean save(Bitmap bitmap, String filePath, Bitmap.CompressFormat format) {
        return ImageUtils.save(bitmap, filePath, format);
    }

    @Override
    public boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat format) {
        return ImageUtils.save(bitmap, file, format);
    }


    @Override
    public void init(Context context) {

    }
}
