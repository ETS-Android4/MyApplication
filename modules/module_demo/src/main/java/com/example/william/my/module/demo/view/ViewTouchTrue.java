package com.example.william.my.module.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.william.my.module.demo.activity.other.DispatchActivity;

public class ViewTouchTrue extends AppCompatTextView {

    private final Context mContext;

    public ViewTouchTrue(Context context) {
        super(context);
        this.mContext = context;
    }

    public ViewTouchTrue(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ViewTouchTrue(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && mContext instanceof DispatchActivity) {
            ((DispatchActivity) mContext).showLogcat("View             dispatchTouchEvent");
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && mContext instanceof DispatchActivity) {
            ((DispatchActivity) mContext).showLogcat("View             onTouchEvent              :    true");
        }
        return true;
    }
}
