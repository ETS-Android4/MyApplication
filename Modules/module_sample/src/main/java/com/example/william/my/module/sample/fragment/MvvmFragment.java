package com.example.william.my.module.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.core.network.retrofit.observer.WithLoadingTipObserver;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.ArticleAdapter;
import com.example.william.my.module.sample.bean.ArticleDetailBean;
import com.example.william.my.module.sample.model.ArticlesViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class MvvmFragment extends Fragment implements OnRefreshLoadMoreListener {

    private ArticleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;

    private ArticlesViewModel mViewModel;

    public static MvvmFragment newInstance() {
        MvvmFragment fragment = new MvvmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_layout_recycler, container, false);
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

        mViewModel = new ViewModelProvider(this).get(ArticlesViewModel.class);
        subscribeToModel(mViewModel);
    }

    private void subscribeToModel(final ArticlesViewModel viewModel) {
        // Observe comments
//        viewModel.getArticleList().observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<List<ArticleDetailBean>>>() {
//            @Override
//            public void onChanged(RetrofitResponse<List<ArticleDetailBean>> listRetrofitResponse) {
//                if (CollectionUtils.isNotEmpty(listRetrofitResponse.getData())) {
//                    if (viewModel.isFirst()) {
//                        mAdapter.setNewInstance(listRetrofitResponse.getData());
//                    } else {
//                        mAdapter.addData(listRetrofitResponse.getData());
//                    }
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//        });
        viewModel.getArticleList().observe(getViewLifecycleOwner(), new WithLoadingTipObserver<List<ArticleDetailBean>>() {
            @Override
            protected void callback(List<ArticleDetailBean> response) {
                if (viewModel.isFirst()) {
                    mAdapter.setNewInstance(response);
                } else {
                    mAdapter.addData(response);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        viewModel.queryArticleList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mViewModel.loadArticleList();
        mSmartRefreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.queryArticleList();
        mSmartRefreshLayout.finishRefresh(1000);
    }
}
