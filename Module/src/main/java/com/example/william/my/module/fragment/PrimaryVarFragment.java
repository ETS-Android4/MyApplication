package com.example.william.my.module.fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragment;
import com.example.william.my.module.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Fragment.FragmentPrimaryDark)
public class PrimaryVarFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.basics_fragment_primary_var;
    }
}
