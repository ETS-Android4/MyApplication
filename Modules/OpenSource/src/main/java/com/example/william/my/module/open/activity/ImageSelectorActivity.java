package com.example.william.my.module.open.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.open.matisse.GifSizeFilter;
import com.example.william.my.module.open.matisse.Glide4Engine;
import com.example.william.my.module.router.ARouterPath;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

/**
 * Matisse目前使用的Glide版本为4.0之前，Glide4.0之后Api的调用方式有了一些更改，所以加载引擎替换为Glide4Engine
 * <p>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <p>
 * https://github.com/zhihu/Matisse
 */
@Route(path = ARouterPath.OpenSource.OpenSource_ImageSelector)
public class ImageSelectorActivity extends BaseActivity {

    private ImageView mImageView;

    private static final int REQUEST_CODE_CHOOSE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(ImageSelectorActivity.this)
                        .choose(MimeType.ofAll())
                        .capture(true)//是否可以拍照
                        .captureStrategy(new CaptureStrategy(true, getPackageName() + ".fileProvider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .countable(true)//是否显示数字
                        .maxSelectable(9)//最大选择资源数量
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))//添加自定义过滤器
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.open_grid_expected_size))//设置列宽
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//设置屏幕方向
                        .thumbnailScale(0.85f)//图片缩放比例
                        .imageEngine(new Glide4Engine())//选择图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            if (mSelected.size() > 0) {
                mImageView.setImageURI(Matisse.obtainResult(data).get(0));
            }
        }
    }
}
