package com.example.william.my.library.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * onAttach -> onCreateDialog -> onCreateView -> onViewCreated -> onStart
 */
public class BaseDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayout() != 0) {
            return inflater.inflate(getLayout(), container, false);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
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
                if (getWindowAnimationsRes() > 0) {
                    window.setWindowAnimations(getWindowAnimationsRes());
                }
                //Android 5.0以上自定义Dialog时发现无法横向铺满屏幕
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setBackgroundDrawableResource(android.R.color.white);
            }
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction transaction = manager.beginTransaction();
            //在每个add事务前增加一个remove事务，防止连续的add
            transaction.remove(this);
            // commit()方法换成了commitAllowingStateLoss()
            // 解决Can not perform this action after onSaveInstanceState with DialogFragment
            transaction.add(this, tag);
            transaction.commitAllowingStateLoss();
            // 解决java.lang.IllegalStateException: Fragment already added
            manager.executePendingTransactions();
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    protected int getLayout() {
        return 0;
    }

    protected void setAttributes(@NonNull LayoutParams params) {
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.dimAmount = 0.8f;
    }

    protected int getWindowAnimationsRes() {
        return 0;
    }
}
