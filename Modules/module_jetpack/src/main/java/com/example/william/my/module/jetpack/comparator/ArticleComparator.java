package com.example.william.my.module.jetpack.comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.william.my.module.bean.ArticleBean;

public class ArticleComparator extends DiffUtil.ItemCallback<ArticleBean.DataBean.ArticleDetailBean> {

    @Override
    public boolean areItemsTheSame(@NonNull ArticleBean.DataBean.ArticleDetailBean oldItem, @NonNull ArticleBean.DataBean.ArticleDetailBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ArticleBean.DataBean.ArticleDetailBean oldItem, @NonNull ArticleBean.DataBean.ArticleDetailBean newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
