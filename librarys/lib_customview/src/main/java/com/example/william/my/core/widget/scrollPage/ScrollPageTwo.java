package com.example.william.my.core.widget.scrollPage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScrollPageTwo extends ScrollView {

    private float oldY;//上次触摸Y轴位置
    private int t;//向上滑动的距离

    public ScrollPageTwo(@NonNull Context context) {
        this(context, null);
    }

    public ScrollPageTwo(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPageTwo(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = ev.getY();
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float slideY = Y - oldY;
                //slideY>0表示正在向下滑动，t==0表示一定滑动到了顶部
                if (slideY > 0 && t == 0) {
                    //然后让顶级那个scrollView滑动滑动事件
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean performClick() {
        //手指按下的时候，获得滑动事件，也就是让顶级scrollview失去滑动事件
        getParent().getParent().requestDisallowInterceptTouchEvent(true);
        return super.performClick();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
