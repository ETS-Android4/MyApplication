package com.example.william.my.core.widget.scrollpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScrollPageOne extends ScrollView {

    private int screenHeight;

    private float oldY;//上次触摸Y轴位置
    private int t;//向上滑动的距离

    public ScrollPageOne(@NonNull Context context) {
        this(context, null);
    }

    public ScrollPageOne(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPageOne(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = ev.getY();
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                //在滑动的时候获得当前值，并计算得到YS,用来判断是向上滑动还是向下滑动
                float Y = ev.getY();
                float slideY = Y - oldY;
                //得到scrollview里面空间的高度
                int childHeight = this.getChildAt(0).getMeasuredHeight();
                //子控件高度减去scrollview向上滑动的距离
                int padding = childHeight - t;
                //slideY<0表示手指正在向上滑动，padding==mScreenHeight表示本scrollview已经滑动到了底部
                if (slideY < 0 && padding == screenHeight) {
                    //让顶级的scrollview重新获得滑动事件
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
