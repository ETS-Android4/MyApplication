package com.example.william.my.module.kotlin.comparator

import androidx.recyclerview.widget.DiffUtil
import com.example.william.my.module.bean.ArticleBean

class ArticleComparator : DiffUtil.ItemCallback<ArticleBean.DataBean.ArticleDetailBean>() {

    override fun areItemsTheSame(
        oldItem: ArticleBean.DataBean.ArticleDetailBean,
        newItem: ArticleBean.DataBean.ArticleDetailBean
    ): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ArticleBean.DataBean.ArticleDetailBean,
        newItem: ArticleBean.DataBean.ArticleDetailBean
    ): Boolean {
        return newItem.id == oldItem.id
    }
}