package com.example.william.my.module.widget.dialog;

import android.view.Gravity;
import android.view.WindowManager;

import com.example.william.my.library.base.BaseDialogFragment;
import com.example.william.my.module.widget.R;

public class MyCreateViewDialog extends BaseDialogFragment {

    @Override
    public int getLayout() {
        return R.layout.widget_dialog_my;
    }

    @Override
    public void setAttributes(WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.gravity = Gravity.BOTTOM;
    }

    @Override
    public int getWindowAnimationsRes() {
        return R.style.Basics_WindowAnimTheme_Bottom;
    }
}
