package com.example.william.my.library.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

public abstract class BaseRecyclerDialogFragment<T> extends BaseDialogFragment
        implements OnRefreshLoadMoreListener, OnItemClickListener, OnItemChildClickListener, OnItemLongClickListener {

    private FrameLayout mFrameLayout;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;

    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.basics_fragment_recycler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView(view);

        initRecyclerData(savedInstanceState);
    }

    private void initRecyclerView(@NonNull View view) {
        mFrameLayout = view.findViewById(R.id.head);
        if (addHeadView() != null) {
            mFrameLayout.addView(addHeadView());
        }
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefresh);

        mAdapter = setAdapter();
        if (mRecyclerView != null) {
            //取消recyclerview单独的滑动效果
            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setLayoutManager(setLayoutManager());

            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemLongClickListener(this);
            mAdapter.setOnItemChildClickListener(this);
        }

        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setEnableRefresh(canRefresh());
            mSmartRefreshLayout.setEnableLoadMore(canLoadMore());

            mSmartRefreshLayout.setOnRefreshLoadMoreListener(this);
        }
    }

    protected View addHeadView() {
        return null;
    }

    protected void initRecyclerData(Bundle savedInstanceState) {

    }

    protected boolean canRefresh() {
        return true;
    }

    protected boolean canLoadMore() {
        return false;
    }

    protected RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> setAdapter();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mSmartRefreshLayout.finishRefresh(1000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mSmartRefreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        return false;
    }
}