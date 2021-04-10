package com.example.william.my.module.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.kotlin.databinding.KItemRecycleBinding

class ArticleAdapter(diffCallback: DiffUtil.ItemCallback<ArticleBean.DataBean.ArticleDetailBean>) :
    PagingDataAdapter<ArticleBean.DataBean.ArticleDetailBean, ArticleAdapter.ViewHolder>(
        diffCallback
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = position.toString() + ". " + getItem(position)?.title
        holder.itemBind.itemRecycleTextView.text = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind =
            KItemRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    class ViewHolder(bind: KItemRecycleBinding) : RecyclerView.ViewHolder(bind.root) {
        var itemBind: KItemRecycleBinding = bind
    }
}