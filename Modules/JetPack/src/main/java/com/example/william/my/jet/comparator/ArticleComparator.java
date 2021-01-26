package com.example.william.my.jet.comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.william.my.module.bean.ArticlesBean;

public class ArticleComparator extends DiffUtil.ItemCallback<ArticlesBean.DataBean.ArticleBean> {

    @Override
    public boolean areItemsTheSame(@NonNull ArticlesBean.DataBean.ArticleBean oldItem, @NonNull ArticlesBean.DataBean.ArticleBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ArticlesBean.DataBean.ArticleBean oldItem, @NonNull ArticlesBean.DataBean.ArticleBean newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
