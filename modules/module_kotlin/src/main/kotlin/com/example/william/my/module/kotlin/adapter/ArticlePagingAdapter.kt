package com.example.william.my.module.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.module.kotlin.databinding.KtItemRecycleBinding

class ArticlePagingAdapter(diffCallback: DiffUtil.ItemCallback<ArticleDetailBean>) :
    PagingDataAdapter<ArticleDetailBean, ArticlePagingAdapter.ViewHolder>(
        diffCallback
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = position.toString() + ". " + getItem(position)?.title
        holder.itemBind.itemRecycleTextView.text = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind =
            KtItemRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    class ViewHolder(bind: KtItemRecycleBinding) : RecyclerView.ViewHolder(bind.root) {
        var itemBind: KtItemRecycleBinding = bind
    }
}