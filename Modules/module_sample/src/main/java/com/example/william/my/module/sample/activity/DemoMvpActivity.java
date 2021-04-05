package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FragmentUtils;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.repo.ArticlesRepository;
import com.example.william.my.module.sample.fragment.MvpFragment;
import com.example.william.my.module.sample.presenter.ArticlesPresenter;

public class DemoMvpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout_fragment);

        MvpFragment mvpFragment = (MvpFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (mvpFragment == null) {
            mvpFragment = MvpFragment.newInstance();
            FragmentUtils.add(getSupportFragmentManager(), mvpFragment, R.id.contentFrame);
        }

        // Create the presenter
        new ArticlesPresenter(ArticlesRepository.getInstance(), mvpFragment);
    }
}