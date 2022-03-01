package com.example.william.my.module.opensource.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/airbnb/lottie-android
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Lottie)
public class LottieActivity extends AppCompatActivity {

    private LottieAnimationView mLottieAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_lottie);

        mLottieAnim = findViewById(R.id.lottie);
        mLottieAnim.addAnimatorListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLottieAnim.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }
        );
    }
}