package com.example.william.my.module.opensource.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.opensource.R;

public class BRVAHItemProvider extends BaseItemProvider<String> {

    @Override
    public int getItemViewType() {
        return 999;
    }

    @Override
    public int getLayoutId() {
        return R.layout.open_item_recycler;
    }

    @Override
    public void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.item_textView, s);
    }
}
