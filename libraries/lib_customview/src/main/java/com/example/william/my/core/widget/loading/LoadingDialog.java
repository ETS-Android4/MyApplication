package com.example.william.my.core.widget.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.example.william.my.core.widget.R;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.basics_dialog_loading);

        //背景不变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount = 0f;
        getWindow().setAttributes(params);

        setCancelable(false);//点击屏幕或物理返回键，dialog不消失
        setCanceledOnTouchOutside(false);//点击屏幕，dialog不消失；点击物理返回键dialog消失
    }
}
