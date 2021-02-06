package com.example.william.my.module.sample.dialog;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.william.my.library.dialog.BaseDialogFragment;
import com.example.william.my.module.sample.R;

public class MyDialog3 extends BaseDialogFragment {

    private static FragmentManager mFragmentManager;

    @Override
    public void onStart() {
        super.onStart();
        mFragmentManager = getChildFragmentManager();
    }

    public static final class Builder extends BaseDialogFragment.Builder<MyDialog3.Builder> {

        public Builder(FragmentActivity mActivity) {
            super(mActivity);
            setContentView(R.layout.basics_layout_response);
        }

    }
}
