package com.example.william.my.module.demo.delegate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class ClickProxyLayout extends FrameLayout {

    public ClickProxyLayout(Context context) {
        super(context);
    }

    public ClickProxyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickProxyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View rootView = this.getRootView();
            ClickDelegate clickDelegate = new ClickDelegate();
            clickDelegate.setDelegate(rootView);
        }
        return super.dispatchTouchEvent(ev);
    }
}