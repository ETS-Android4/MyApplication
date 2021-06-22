package com.example.william.my.module.open.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public class BRVAHItemProvider extends BaseItemProvider<String> {

    @Override
    public int getItemViewType() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void convert(@NonNull BaseViewHolder baseViewHolder, String s) {

    }
}
