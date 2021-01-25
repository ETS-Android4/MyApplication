package com.example.william.my.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.kotlin.databinding.KotlinItemRecycleBinding
import com.example.william.my.module.bean.ArticlesBean

class ArticlesAdapter(diffCallback: DiffUtil.ItemCallback<ArticlesBean.DataBean.ArticleBean>) :
    PagingDataAdapter<ArticlesBean.DataBean.ArticleBean, ArticlesAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = position.toString() + ". " + getItem(position)?.title
        holder.itemBind.itemRecycleTextView.text = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind =
            KotlinItemRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    class ViewHolder(bind: KotlinItemRecycleBinding) : RecyclerView.ViewHolder(bind.root) {
        var itemBind: KotlinItemRecycleBinding = bind
    }
}