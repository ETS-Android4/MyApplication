package com.example.william.my.module.jetpack.comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.william.my.module.bean.ArticleDetailBean;

public class ArticleComparator extends DiffUtil.ItemCallback<ArticleDetailBean> {

    @Override
    public boolean areItemsTheSame(@NonNull ArticleDetailBean oldItem, @NonNull ArticleDetailBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ArticleDetailBean oldItem, @NonNull ArticleDetailBean newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
