package com.example.william.my.module.sample.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.ArticleAdapter;
import com.example.william.my.module.sample.contract.ArticleContract;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class MvpFragment extends Fragment implements ArticleContract.View, OnRefreshLoadMoreListener {

    private ArticleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;

    private ArticleContract.Presenter mPresenter;

    public static MvpFragment newInstance() {
        MvpFragment fragment = new MvpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sample_layout_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycleView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefresh);

        mAdapter = new ArticleAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSmartRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onRefreshArticleList();
    }

    @Override
    public void setPresenter(@NonNull ArticleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        ToastUtils.showLong("正在请求数据…");
    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showToast(String message) {
        ToastUtils.showShort(message);
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void showArticles(int page, List<ArticleDetailBean> article) {
        if (page == 0) {
            mAdapter.setNewInstance(article);
        } else {
            mAdapter.addData(article);
        }
        mSmartRefreshLayout.setEnableLoadMore(true);
    }

    @Override
    public void showEmptyView() {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("无数据");
        mAdapter.setEmptyView(textView);
        mAdapter.notifyDataSetChanged();
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void showArticlesNoMore() {
        ToastUtils.showShort("无更多数据");
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.onLoadMoreArticleList();
        mSmartRefreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.onRefreshArticleList();
        mSmartRefreshLayout.finishRefresh(1000);
    }
}
