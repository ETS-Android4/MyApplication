package com.example.william.my.module.open.adapter;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

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
    public void convert(@NotNull BaseViewHolder baseViewHolder, String s) {

    }
}
