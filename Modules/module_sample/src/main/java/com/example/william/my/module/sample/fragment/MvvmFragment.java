package com.example.william.my.module.sample.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.william.my.core.retrofit.observer.WithLoadingTipObserver;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.status.State;
import com.example.william.my.module.bean.ArticleDataBean;
import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.ArticleAdapter;
import com.example.william.my.module.sample.model.ArticleViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class MvvmFragment extends Fragment implements OnRefreshLoadMoreListener {

    private ArticleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;

    private ArticleViewModel mViewModel;

    public static MvvmFragment newInstance() {
        MvvmFragment fragment = new MvvmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sample_layout_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycleView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);

        mAdapter = new ArticleAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSmartRefreshLayout.setOnRefreshLoadMoreListener(this);

        subscribeToModel();
    }

    private void subscribeToModel() {

        mViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        //getArticleListByObserver();
        //getArticleListByWithLoadingTipObserver();

        //getArticleByObserver();
        getArticleByWithLoadingTipObserver();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mViewModel.queryArticleList();
        mViewModel.onRefreshArticle();
    }

    private void showLoading() {
        ToastUtils.showShort("正在请求数据…");
    }

    public void showToast(String message) {
        showEmptyView();
        ToastUtils.showShort(message);
    }

    private void onDataNotAvailable(boolean isFirst) {
        if (isFirst) {
            showEmptyView();
        } else {
            onDataNoMore();
        }
    }

    public void showEmptyView() {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("无数据");
        mAdapter.setEmptyView(textView);
        mAdapter.notifyDataSetChanged();
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    public void onDataNoMore() {
        ToastUtils.showShort("无更多数据");
    }

    private void showArticles(boolean isFirst, List<ArticleDetailBean> articles) {
        if (isFirst) {
            mAdapter.setNewInstance(articles);
        } else {
            mAdapter.addData(articles);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //mViewModel.onLoadMoreArticleList();
        mViewModel.onLoadMoreArticle();
        mSmartRefreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //mViewModel.onRefreshArticleList();
        mViewModel.onRefreshArticle();
        mSmartRefreshLayout.finishRefresh(1000);
    }

    /**
     * getArticleList -> Observer
     */
    private void getArticleListByObserver() {
        mViewModel.getArticleList().observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<List<ArticleDetailBean>>>() {
            @Override
            public void onChanged(RetrofitResponse<List<ArticleDetailBean>> response) {
                if (response.getCode() == State.LOADING) {
                    showLoading();
                } else if (response.getCode() == State.SUCCESS) {
                    if (CollectionUtils.isEmpty(response.getData())) {
                        onDataNotAvailable(mViewModel.isFirst());
                    } else {
                        showArticles(mViewModel.isFirst(), response.getData());
                    }
                } else if (response.getCode() == State.ERROR) {
                    showToast(response.getMessage());
                }
            }
        });
    }

    /**
     * getArticleList -> WithLoadingTipObserver
     */
    private void getArticleListByWithLoadingTipObserver() {
        mViewModel.getArticleList().observe(getViewLifecycleOwner(), new WithLoadingTipObserver<List<ArticleDetailBean>>() {
            @Override
            protected void onResponse(@NonNull List<ArticleDetailBean> response) {
                if (CollectionUtils.isEmpty(response)) {
                    onDataNotAvailable(mViewModel.isFirst());
                } else {
                    showArticles(mViewModel.isFirst(), response);
                }
            }

            @Override
            protected boolean onFailure(String msg) {
                showToast(msg);
                return super.onFailure(msg);
            }
        });
    }

    /**
     * getArticle -> Observer
     */
    private void getArticleByObserver() {
        mViewModel.getArticle().observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<ArticleDataBean>>() {
            @Override
            public void onChanged(RetrofitResponse<ArticleDataBean> response) {
                if (response.getCode() == State.LOADING) {
                    showLoading();
                } else if (response.getCode() == State.SUCCESS) {
                    if (ObjectUtils.isNotEmpty(response.getData())) {
                        if (CollectionUtils.isEmpty(response.getData().getDatas())) {
                            onDataNotAvailable(response.getData().getCurPage() == 1);
                        } else {
                            showArticles(response.getData().getCurPage() == 1, response.getData().getDatas());
                        }
                    } else {
                        showToast(response.getMessage());
                    }
                } else if (response.getCode() == State.ERROR) {
                    showToast(response.getMessage());
                }
            }
        });
    }

    /**
     * getArticle -> WithLoadingTipObserver
     */
    private void getArticleByWithLoadingTipObserver() {
        mViewModel.getArticle().observe(getViewLifecycleOwner(), new WithLoadingTipObserver<ArticleDataBean>() {
            @Override
            protected void onResponse(@NonNull ArticleDataBean response) {
                if (CollectionUtils.isEmpty(response.getDatas())) {
                    onDataNotAvailable(response.getCurPage() == 1);
                } else {
                    showArticles(response.getCurPage() == 1, response.getDatas());
                }
            }

            @Override
            protected boolean onFailure(String msg) {
                showToast(msg);
                return super.onFailure(msg);
            }
        });
    }
}
