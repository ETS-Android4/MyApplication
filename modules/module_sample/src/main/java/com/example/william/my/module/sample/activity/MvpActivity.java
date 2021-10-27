package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FragmentUtils;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.fragment.MvpFragment;
import com.example.william.my.module.sample.presenter.ArticlePresenter;
import com.example.william.my.module.sample.repo.ArticleRepository;

@Route(path = ARouterPath.Sample.Sample_MVP)
public class MvpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout_fragment);

        MvpFragment mvpFragment = (MvpFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (mvpFragment == null) {
            mvpFragment = MvpFragment.newInstance();
            FragmentUtils.add(getSupportFragmentManager(), mvpFragment, R.id.contentFrame);
        }

        // Create the presenter
        new ArticlePresenter(ArticleRepository.getInstance(), mvpFragment);
    }
}