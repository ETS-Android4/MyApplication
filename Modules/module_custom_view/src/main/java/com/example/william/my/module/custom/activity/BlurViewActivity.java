package com.example.william.my.module.custom.activity;

import android.os.Bundle;
import android.widget.SeekBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.blurview.BlurView;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * 高斯模糊
 */
@Route(path = ARouterPath.CustomView.CustomView_BlurView)
public class BlurViewActivity extends BaseActivity {

    private BlurView mBlurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_blur);

        mBlurView = findViewById(R.id.blur_blurView);
        mBlurView.setImageView(R.drawable.ic_launcher);

        SeekBar blur_seekBar = findViewById(R.id.blur_seekBar);
        blur_seekBar.setMax(100);
        blur_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBlurView.setImageBlur(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}