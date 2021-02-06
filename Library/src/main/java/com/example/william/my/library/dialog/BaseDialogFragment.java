package com.example.william.my.library.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
