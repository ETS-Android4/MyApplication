package com.example.william.my.module.sample.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.module.bean.ArticleDetailBean
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleItemRecyclerBinding

class ArticleViewBindAdapter :
    BaseQuickAdapter<ArticleDetailBean?, ArticleViewBindAdapter.ViewBindingHolder>(R.layout.sample_item_recycler) {

    override fun convert(holder: ViewBindingHolder, item: ArticleDetailBean?) {
        item?.run {
            holder.itemBind.itemTextView.text = title
        }
    }

    class ViewBindingHolder(bind: SampleItemRecyclerBinding) : BaseViewHolder(bind.root) {
        var itemBind: SampleItemRecyclerBinding = bind
    }
}
