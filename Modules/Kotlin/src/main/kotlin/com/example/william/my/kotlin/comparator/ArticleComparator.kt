package com.example.william.my.kotlin.comparator

import androidx.recyclerview.widget.DiffUtil
import com.example.william.my.module.bean.ArticlesBean

class ArticleComparator : DiffUtil.ItemCallback<ArticlesBean.DataBean.ArticleBean>() {

    override fun areItemsTheSame(
        oldItem: ArticlesBean.DataBean.ArticleBean,
        newItem: ArticlesBean.DataBean.ArticleBean
    ): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ArticlesBean.DataBean.ArticleBean,
        newItem: ArticlesBean.DataBean.ArticleBean
    ): Boolean {
        return newItem.id == oldItem.id
    }
}