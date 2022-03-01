package com.example.william.my.module.util.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ImageUtils;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.util.R;

import java.io.File;

@Route(path = ARouterPath.Util.Util_ImageUtils)
public class ImageUtilsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        ImageView mImageView = findViewById(R.id.basics_imageView);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher);
        //mImageView.setImageDrawable(drawable);
        Bitmap drawable2Bitmap = ImageUtils.drawable2Bitmap(drawable);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //mImageView.setImageBitmap(bitmap);
        Drawable bitmap2Drawable = ImageUtils.bitmap2Drawable(bitmap);

        //采样压缩 压缩格式为JPEG
        mImageView.setImageBitmap(ImageUtils.compressBySampleSize(bitmap, 2));

        //缩放压缩
        mImageView.setImageBitmap(ImageUtils.compressByScale(bitmap, 0.5f, 0.5f));

        String filePath = getExternalCacheDir() + File.separator + "launcher.png";

        //ImageUtils.save(bitmap, filePath, Bitmap.CompressFormat.PNG);
        //ImageUtils.save(bitmap, new File(filePath), Bitmap.CompressFormat.PNG);

        //保存图片到相册，随机文件名
        //File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.PNG);
    }
}