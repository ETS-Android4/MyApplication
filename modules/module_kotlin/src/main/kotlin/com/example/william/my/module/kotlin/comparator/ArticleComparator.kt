package com.example.william.my.module.kotlin.comparator

import androidx.recyclerview.widget.DiffUtil
import com.example.william.my.retrofit.ArticleDetailBean

class ArticleComparator : DiffUtil.ItemCallback<com.example.william.my.retrofit.ArticleDetailBean>() {

    override fun areItemsTheSame(
        oldItem: com.example.william.my.retrofit.ArticleDetailBean,
        newItem: com.example.william.my.retrofit.ArticleDetailBean
    ): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: com.example.william.my.retrofit.ArticleDetailBean,
        newItem: com.example.william.my.retrofit.ArticleDetailBean
    ): Boolean {
        return newItem.id == oldItem.id
    }
}