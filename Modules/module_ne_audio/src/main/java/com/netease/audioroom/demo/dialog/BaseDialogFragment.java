package com.netease.audioroom.demo.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseDialogFragment extends DialogFragment {

    public String TAG = getClass().getName();

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
        try {
            FragmentTransaction transaction = manager.beginTransaction();
            //在每个add事务前增加一个remove事务，防止连续的add
            transaction.remove(this).commit();
            //super.show(manager, tag);
            //解决Can not perform this action after onSaveInstanceState with DialogFragment
            transaction.commitAllowingStateLoss();
            //解决java.lang.IllegalStateException: Fragment already added
            manager.executePendingTransactions();
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }
}
