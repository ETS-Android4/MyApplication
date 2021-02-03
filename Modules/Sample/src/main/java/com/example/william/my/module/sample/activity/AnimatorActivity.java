package com.example.william.my.module.sample.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;

@Route(path = ARouterPath.Sample.Sample_Animator)
public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {

    private int index = -1;

    private ImageView mImageView;

    private ObjectAnimator animatorAlpha, animatorRotation, animatorScale, animatorTranslation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);
        mImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        index++;
        if (index % 6 == 1) {
            startAnimatorAlpha();
        } else if (index % 6 == 2) {
            startAnimatorRotation();
        } else if (index % 6 == 3) {
            startAnimatorScale();
        } else if (index % 6 == 4) {
            startAnimatorTranslation();
        } else if (index % 6 == 5) {
            startAnimatorSet();
        } else if (index % 6 == 0) {
            startValueAnimator();
        }
    }

    /**
     * 透明度
     */
    private void startAnimatorAlpha() {
        animatorAlpha = ObjectAnimator.ofFloat(mImageView, "alpha", 1f, 0f, 1f);
        animatorAlpha.setDuration(3000);
        //alpha.setRepeatCount(ValueAnimator.INFINITE);//-1 代表无限循环执行
        animatorAlpha.start();
    }

    /**
     * 旋转
     */
    private void startAnimatorRotation() {
        animatorRotation = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f);
        animatorRotation.setDuration(3000);
        animatorRotation.start();
    }

    /**
     * 缩放
     */
    private void startAnimatorScale() {
        animatorScale = ObjectAnimator.ofFloat(mImageView, "scaleX", 1f, 0.5f, 1f);
        animatorScale.setDuration(3000);
        animatorScale.start();
    }

    /**
     * 缩放
     */
    private void startAnimatorTranslation() {
        animatorTranslation = ObjectAnimator.ofFloat(mImageView, "translationX", mImageView.getTranslationX(), -400f, mImageView.getTranslationX());
        animatorTranslation.setDuration(3000);
        animatorTranslation.start();
    }

    /**
     * 动画组合
     */
    private void startAnimatorSet() {
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animatorAlpha, animatorRotation, animatorScale, animatorTranslation);//如果上一个动画无线循环，则无法进行下一个动画
        animatorSet.setDuration(3000);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //animatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            /**
             * 当AnimatorSet重复时调用，由于AnimatorSet没有设置repeat的函数，所以这个方法永远不会被调用
             */
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    /**
     * 差值动画
     */
    private void startValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setDuration(3000);


        valueAnimator.setInterpolator(new AccelerateInterpolator());//加速
        valueAnimator.setInterpolator(new DecelerateInterpolator());//减速
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setInterpolator(new LinearInterpolator());//匀速

        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.setInterpolator(new CycleInterpolator(1));


        //加速插值器，公式： y=t^(2f) （加速度参数. f越大，起始速度越慢，但是速度越来越快）
        //valueAnimator.setInterpolator(new AccelerateInterpolator(10));

        //减速插值器公式: y=1-(1-t)^(2f) （描述: 加速度参数. f越大，起始速度越快，但是速度越来越慢）
        //valueAnimator.setInterpolator(new DecelerateInterpolator());

        //先加速后减速插值器 y=cos((t+1)π)/2+0.5
        //valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        //张力值, 默认为2，T越大，初始的偏移越大，而且速度越快 公式：y=(T+1)×t3–T×t2
        //valueAnimator.setInterpolator(new AnticipateInterpolator());

        //张力值tension，默认为2，张力越大，起始和结束时的偏移越大，
        //而且速度越快;额外张力值extraTension，默认为1.5。公式中T的值为tension*extraTension
        //valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());

        //弹跳插值器
        //valueAnimator.setInterpolator(new BounceInterpolator());

        //周期插值器 y=sin(2π×C×t)  周期值，默认为1；2表示动画会执行两次
        //valueAnimator.setInterpolator(new CycleInterpolator(2));

        //线性插值器，匀速公式：Y=T
        //valueAnimator.setInterpolator(new LinearInterpolator());

        //公式: y=(T+1)x(t1)3+T×(t1)2 +1
        //描述: 张力值，默认为2，T越大，结束时的偏移越大，而且速度越快
        valueAnimator.setInterpolator(new OvershootInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mImageView.setRotation(value);
            }
        });
        valueAnimator.start();
    }

}