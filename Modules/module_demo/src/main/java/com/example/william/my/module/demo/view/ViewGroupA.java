package com.example.william.my.module.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.william.my.module.demo.activity.TouchEventActivity;

public class ViewGroupA extends ConstraintLayout {

    private static final String TAG = "ViewGroupA   [经理]";

    public ViewGroupA(@NonNull Context context) {
        super(context);
    }

    public ViewGroupA(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupA(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 分发事件
     *
     * @return 受当前onTouchEvent和下级view的dispatchTouchEvent影响
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (TouchEventActivity.plan == 1) {
                Log.e(TAG, "dispatchTouchEvent     " + "技术部,老板说按钮不好看,要加一道光.");
            } else if (TouchEventActivity.plan == 2) {
                Log.e(TAG, "dispatchTouchEvent     " + "技术部,老板要做淘宝,下周上线.");
            } else if (TouchEventActivity.plan == 3) {
                Log.e(TAG, "dispatchTouchEvent     " + "技术部,你们的app快做完了么?");
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 拦截事件
     * ViewGroup特有方法
     *
     * @return 默认返回 false，不拦截任何事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "onInterceptTouchEvent  ");
        }
        if (TouchEventActivity.plan == 3) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * 消费事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (TouchEventActivity.plan == 1) {
                Log.e(TAG, "onTouchEvent           ");
            } else if (TouchEventActivity.plan == 2) {
                Log.e(TAG, "onTouchEvent           " + "报告老板, 技术部说做不了");
            } else if (TouchEventActivity.plan == 3) {
                Log.e(TAG, "onTouchEvent           " + "正在测试,明天就测试完了");
            }
        }
        if (TouchEventActivity.plan == 3) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }
}
