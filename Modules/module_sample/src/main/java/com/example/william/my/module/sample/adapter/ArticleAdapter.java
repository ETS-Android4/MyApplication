package com.example.william.my.module.sample.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.bean.ArticleDetailBean;

public class ArticleAdapter extends BaseQuickAdapter<ArticleDetailBean, BaseViewHolder> {

    public ArticleAdapter() {
        super(R.layout.demo_item_recycler);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleDetailBean item) {
        helper.setText(R.id.item_textView, item.getTitle());
    }
}
