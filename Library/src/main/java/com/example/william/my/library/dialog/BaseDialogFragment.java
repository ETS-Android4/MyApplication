package com.example.william.my.library.dialog;

import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            //WindowManager.LayoutParams params = window.getAttributes();
            //params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //window.setAttributes(params);
            //Android 5.0以上自定义Dialog时发现无法横向铺满屏幕
            //window.getDecorView().setPadding(0, 0, 0, 0);
            //window.setBackgroundDrawableResource(android.R.color.white);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getDialog() != null) {
            try {
                // 解决Dialog内存泄漏
                getDialog().setOnDismissListener(null);
                getDialog().setOnCancelListener(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
