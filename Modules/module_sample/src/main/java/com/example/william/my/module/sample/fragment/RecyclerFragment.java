package com.example.william.my.module.sample.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerFragment;
import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.sample.adapter.ArticleAdapter;
import com.example.william.my.module.sample.contract.ArticleContract;
import com.example.william.my.module.sample.presenter.ArticlePresenter;
import com.example.william.my.module.sample.repo.ArticleRepository;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class RecyclerFragment extends BaseRecyclerFragment<ArticleDetailBean> implements ArticleContract.View {

    private ArticleContract.Presenter mPresenter;

    @Override
    public BaseQuickAdapter<ArticleDetailBean, BaseViewHolder> setAdapter() {
        return new ArticleAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new ArticlePresenter(ArticleRepository.getInstance(), this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
    }

    @Override
    public void showEmptyView() {
        onEmptyView();
    }

    @Override
    public void showArticles(boolean isFirst, List<ArticleDetailBean> article) {
        onDataSuccess(article);
    }

    @Override
    public void onDataNoMore() {
        onDataNoMore();
    }

    @Override
    public void setPresenter(ArticleContract.Presenter presenter) {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
