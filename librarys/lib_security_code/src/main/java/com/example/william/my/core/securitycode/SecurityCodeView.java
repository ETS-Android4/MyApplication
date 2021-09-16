package com.example.william.my.core.securitycode;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SecurityCodeView extends RelativeLayout {

    public SecurityCodeView(Context context) {
        this(context, null);
    }

    public SecurityCodeView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public SecurityCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

    }

    private void initView(Context context) {

    }
}
