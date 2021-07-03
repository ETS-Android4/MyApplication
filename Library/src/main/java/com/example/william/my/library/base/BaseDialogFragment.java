package com.example.william.my.library.base;

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
import androidx.fragment.app.FragmentManager;

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

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
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
