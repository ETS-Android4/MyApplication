package com.example.william.my.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;

public class BaseDialogFragment extends DialogFragment {

    private static Dialog mDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() != null) {
            try {
                // 解决Dialog内存泄漏
                mDialog = getDialog();
                getDialog().setOnDismissListener(null);
                getDialog().setOnCancelListener(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Builder<T extends BaseDialogFragment.Builder<T>> extends AppCompatDialog {

        private final FragmentActivity mActivity;
        private WeakReference<BaseDialogFragment> mDialogFragment;

        public Builder(FragmentActivity context) {
            super(context);
            this.mActivity = context;
            this.mDialogFragment = new WeakReference<>(new BaseDialogFragment());
        }

        @Override
        public void show() {
            super.show();
        }
    }
}
