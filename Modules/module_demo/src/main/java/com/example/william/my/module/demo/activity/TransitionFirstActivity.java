package com.example.william.my.module.demo.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * 视图过度动画
 */
@Route(path = ARouterPath.Demo.Demo_Transition)
public class TransitionFirstActivity extends BaseActivity implements View.OnClickListener {

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_activity_transition_first);

        mIntent = new Intent(this, TransitionSecondActivity.class);

        findViewById(R.id.transition_explode).setOnClickListener(this);
        findViewById(R.id.transition_slide).setOnClickListener(this);
        findViewById(R.id.transition_fade).setOnClickListener(this);
        findViewById(R.id.transition_share).setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.transition_explode) {//分解
            mIntent.putExtra("transition", "explode");
            startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(TransitionFirstActivity.this).toBundle());
        } else if (i == R.id.transition_slide) {//滑动
            mIntent.putExtra("transition", "slide");
            startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(TransitionFirstActivity.this).toBundle());
        } else if (i == R.id.transition_fade) {//淡入
            mIntent.putExtra("transition", "fade");
            startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(TransitionFirstActivity.this).toBundle());
        } else if (i == R.id.transition_share) {//共享元素
            mIntent.putExtra("transition", "share");
            //将原先的跳转改成如下方式，注意这里面的第三个参数决定了ActivityTwo 布局中的android:transitionName的值，它们要保持一致
            startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(TransitionFirstActivity.this,
                    findViewById(R.id.transition_share), "shareTransition").toBundle());
        }
    }
}