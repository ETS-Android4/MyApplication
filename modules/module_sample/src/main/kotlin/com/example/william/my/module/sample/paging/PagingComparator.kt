package com.example.william.my.module.sample.paging

import androidx.recyclerview.widget.DiffUtil
import com.example.william.my.bean.data.ArticleDetailBean

class PagingComparator : DiffUtil.ItemCallback<ArticleDetailBean>() {

    override fun areItemsTheSame(oldItem: ArticleDetailBean, newItem: ArticleDetailBean): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleDetailBean, newItem: ArticleDetailBean): Boolean {
        return newItem.id == oldItem.id
    }
}