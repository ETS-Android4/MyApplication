package com.example.william.my.module.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class BaseRecyclerFragment<T> extends Fragment {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;

    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);

        mRecyclerView.setLayoutManager(setLayoutManager());

        mAdapter = setAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    public BaseQuickAdapter<T, BaseViewHolder> setAdapter() {
        return null;
    }
}
