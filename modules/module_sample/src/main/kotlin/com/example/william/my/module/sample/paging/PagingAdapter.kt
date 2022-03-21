package com.example.william.my.module.sample.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.module.sample.databinding.SampleItemRecyclerBinding

/**
 * Paging RecyclerView 适配器
 */
class PagingAdapter(diffCallback: DiffUtil.ItemCallback<ArticleDetailBean>) :
    PagingDataAdapter<ArticleDetailBean, PagingAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = position.toString() + ". " + getItem(position)?.title
        holder.itemBind.itemTextView.text = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind = SampleItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    class ViewHolder(bind: SampleItemRecyclerBinding) : RecyclerView.ViewHolder(bind.root) {
        var itemBind: SampleItemRecyclerBinding = bind
    }
}