package com.example.william.my.module.demo.delegate;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;

public class ClickDelegate extends View.AccessibilityDelegate {

    @Override
    public void sendAccessibilityEvent(View host, int eventType) {
        super.sendAccessibilityEvent(host, eventType);
        if (AccessibilityEvent.TYPE_VIEW_CLICKED == eventType) {
            sendLog();
        }
    }

    public void sendLog() {
    }

    public ClickDelegate(final View rootView) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setDelegate(rootView);
            }
        });
    }

    public void setDelegate(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = group.getChildAt(i);
                    setDelegate(view);
                }
            } else {
                if (view.isClickable()) {
                    view.setAccessibilityDelegate(this);
                }
            }
        }
    }

    public ClickDelegate() {

    }
}