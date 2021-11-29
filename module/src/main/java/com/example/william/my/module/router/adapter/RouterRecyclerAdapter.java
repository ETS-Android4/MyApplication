package com.example.william.my.module.router.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.R;
import com.example.william.my.module.router.item.RouterItem;

public class RouterRecyclerAdapter extends BaseQuickAdapter<RouterItem, BaseViewHolder> {

    public RouterRecyclerAdapter() {
        super(R.layout.basics_item_recycler);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RouterItem item) {
        helper.setText(R.id.item_textView, item.mRouterName);
    }
}