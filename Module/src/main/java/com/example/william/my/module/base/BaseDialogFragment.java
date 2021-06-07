package com.example.william.my.module.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * onAttach -> onCreateDialog -> onCreateView -> onViewCreated -> onStart
 */
public class BaseDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            //解决Dialog内存泄漏
            dialog.setOnDismissListener(null);
            dialog.setOnCancelListener(null);

            Window window = dialog.getWindow();
            if (window != null) {
                LayoutParams params = window.getAttributes();
                setAttributes(params);
                window.setAttributes(params);
                //Android 5.0以上自定义Dialog时发现无法横向铺满屏幕
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setBackgroundDrawableResource(android.R.color.white);
                if (getWindowAnimationsRes() > 0) {
                    window.setWindowAnimations(getWindowAnimationsRes());
                }
            }
        }
    }

    public int getLayout() {
        return 0;
    }

    public void setAttributes(LayoutParams params) {
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.WRAP_CONTENT;
    }

    public int getWindowAnimationsRes() {
        return 0;
    }
}
