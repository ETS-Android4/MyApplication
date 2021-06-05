package com.example.william.my.module.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (setDialog() != null) {
            return setDialog();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    public Dialog setDialog() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setLayout() != 0) {
            return inflater.inflate(setLayout(), container);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public int setLayout() {
        return 0;
    }
}
