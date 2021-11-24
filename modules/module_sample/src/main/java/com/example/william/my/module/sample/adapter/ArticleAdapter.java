package com.example.william.my.module.sample.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.bean.data.ArticleDetailBean;
import com.example.william.my.module.sample.R;

public class ArticleAdapter extends BaseQuickAdapter<ArticleDetailBean, BaseViewHolder> {

    public ArticleAdapter() {
        super(R.layout.sample_item_recycler);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleDetailBean item) {
        helper.setText(R.id.item_textView, item.getTitle());
    }
}