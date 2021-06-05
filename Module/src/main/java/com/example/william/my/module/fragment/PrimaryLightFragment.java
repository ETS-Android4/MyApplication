package com.example.william.my.module.fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.R;
import com.example.william.my.module.base.BaseFragment;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Fragment.FragmentPrimaryLight)
public class PrimaryLightFragment extends BaseFragment {

    @Override
    protected int createView() {
        return R.layout.basics_fragment_primary_light;
    }
}
