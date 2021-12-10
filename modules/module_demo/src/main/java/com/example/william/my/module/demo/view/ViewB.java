package com.example.william.my.module.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.william.my.module.demo.activity.other.TouchEventActivity;
import com.example.william.my.module.utils.L;

public class ViewB extends View {

    private static final String TAG = "ViewB        [码农]";

    public ViewB(Context context) {
        super(context);
    }

    public ViewB(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewB(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (TouchEventActivity.plan == 1) {
                L.e(TAG, "dispatchTouchEvent     " + "加一道光.");
            } else if (TouchEventActivity.plan == 2) {
                L.e(TAG, "dispatchTouchEvent     " + "做淘宝???");
            } else if (TouchEventActivity.plan == 3) {
                L.e(TAG, "dispatchTouchEvent     " + "现在项目做到什么程度了?");
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (TouchEventActivity.plan == 1) {
                L.e(TAG, "onTouchEvent           " + "做好了.");
            } else if (TouchEventActivity.plan == 2) {
                L.e(TAG, "onTouchEvent           " + "这个真心做不了啊");
            } else if (TouchEventActivity.plan == 3) {
                L.e(TAG, "onTouchEvent           " + "现在项目做到什么程度了?");
            }
        }
        if (TouchEventActivity.plan == 1) {
            return true;
        } else if (TouchEventActivity.plan == 2) {
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }
}
