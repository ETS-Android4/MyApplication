package com.example.william.my.module.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Fragment.FragmentPrimaryLight)
public class PrimaryLightFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basics_fragment_primary_light, container, false);
    }

}
