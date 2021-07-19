package com.example.william.my.library.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseRecyclerDialogFragment<T> extends BaseDialogFragment
        implements OnItemClickListener, OnItemChildClickListener, OnRefreshLoadMoreListener, OnItemLongClickListener {

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
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);

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

    protected void onDataFail(String message) {
        mSmartRefreshLayout.setEnableLoadMore(false);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void onDataSuccess(boolean isFirst, List<T> list) {
        if (list != null && !list.isEmpty()) {
            if (isFirst) {
                mAdapter.setNewInstance(list);
            } else {
                mAdapter.addData(list);
            }
            mSmartRefreshLayout.setEnableLoadMore(true);
        } else {
            mSmartRefreshLayout.setEnableLoadMore(false);
        }
    }

    protected void onEmptyView() {
        onEmptyView("暂无数据");
    }

    protected void onEmptyView(String message) {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText(TextUtils.isEmpty(message) ? "暂无数据" : message);
        mAdapter.setEmptyView(textView);
        mAdapter.notifyDataSetChanged();
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    protected void onDataNoMore() {
        mSmartRefreshLayout.setEnableLoadMore(false);
        Toast.makeText(getContext(), "无更多数据", Toast.LENGTH_SHORT).show();
    }

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
    public boolean onItemLongClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
        return false;
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }

}
