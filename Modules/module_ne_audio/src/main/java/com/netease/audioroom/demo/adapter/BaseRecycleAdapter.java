package com.netease.audioroom.demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;

import org.jetbrains.annotations.NotNull;

public class BaseRecycleAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseRecycleAdapter() {
        super(R.layout.item_recycle);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, T t) {
        onBindViewHolder(holder, t);
        if (t instanceof String) {
            holder.setText(R.id.tv_item, t.toString());
        }
    }

    protected void onBindViewHolder(BaseViewHolder holder, T itemData) {

    }
}
