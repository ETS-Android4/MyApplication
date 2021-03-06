package com.example.william.my.module.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.william.my.module.demo.activity.other.DispatchActivity;

public class GroupTouchTrue extends ConstraintLayout {

    private final Context mContext;

    public GroupTouchTrue(Context context) {
        super(context);
        this.mContext = context;
    }

    public GroupTouchTrue(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public GroupTouchTrue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && mContext instanceof DispatchActivity) {
            ((DispatchActivity) mContext).showLogcat("ViewGroup    onInterceptTouchEvent  :    false");
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && mContext instanceof DispatchActivity) {
            ((DispatchActivity) mContext).showLogcat("ViewGroup    dispatchTouchEvent");
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && mContext instanceof DispatchActivity) {
            ((DispatchActivity) mContext).showLogcat("ViewGroup    onTouchEvent              :    true");
        }
        return true;
    }
}
