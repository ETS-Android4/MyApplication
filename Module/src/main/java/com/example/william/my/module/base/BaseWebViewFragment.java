package com.example.william.my.module.base;

import com.example.william.my.library.base.BaseFragment;
import com.example.william.my.module.R;

public class BaseWebViewFragment extends BaseFragment {

    @Override
    protected int createView() {
        return R.layout.basics_fragment_webview;
    }
}
