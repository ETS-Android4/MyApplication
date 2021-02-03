package com.example.william.my.module.sample.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;

import java.util.Random;

/**
 * 图片旋转动画之转盘功能
 */
@Route(path = ARouterPath.Sample.Sample_Turntable)
public class TurntableActivity extends BaseActivity {

    private int startDegree = 0;//初始角度

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);
        mImageView = findViewById(R.id.basics_imageView);
        mImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.im_ic_launcher));
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAward(new Random().nextInt(10));
            }
        });
    }

    private void goAward(int index) {
        int n = 10;//奖品数目
        int lap = 3;//旋转圈数
        int one = 1000;//旋转一圈需要的时间
        final int angle = index * 360 / n;//奖品角度
        int increaseDegree = lap * 360 + angle;//目标角度
        //初始化旋转动画，后面的四个参数是用来设置以自己的中心点为圆心转圈
        RotateAnimation rotateAnimation = new RotateAnimation(startDegree, increaseDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //计算动画播放总时间index
        long time = (lap + angle / 360) * one;
        //设置动画播放时间
        rotateAnimation.setDuration(time);
        //设置动画播放完后，停留在最后一帧画面上
        rotateAnimation.setFillAfter(true);
        //设置动画的加速行为，是先加速后减速
        rotateAnimation.setInterpolator(TurntableActivity.this, android.R.anim.accelerate_decelerate_interpolator);
        //设置动画的监听器
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //将最后的角度赋值给startDegree作为下次转圈的初始角度
                startDegree = angle;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //开始播放动画
        mImageView.startAnimation(rotateAnimation);
    }
}
