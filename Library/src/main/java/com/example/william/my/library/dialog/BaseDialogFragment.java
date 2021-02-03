package com.example.william.my.library.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        //super.show(manager, tag);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, tag);
        transaction.commitAllowingStateLoss();
    }
}
