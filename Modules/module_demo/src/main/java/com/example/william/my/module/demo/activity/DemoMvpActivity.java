package com.example.william.my.module.demo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FragmentUtils;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.data.ArticleRepository;
import com.example.william.my.module.demo.fragment.ArticleFragment;
import com.example.william.my.module.demo.presenter.ArticlePresenter;

public class DemoMvpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout_fragment);

        ArticleFragment articleFragment = (ArticleFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (articleFragment == null) {
            articleFragment = ArticleFragment.newInstance();
            FragmentUtils.add(getSupportFragmentManager(), articleFragment, R.id.contentFrame);
        }

        // Create the presenter
        new ArticlePresenter(ArticleRepository.getInstance(), articleFragment);
    }
}