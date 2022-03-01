package com.example.william.my.module.sample.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.module.sample.R

class ArticleAdapter :
    BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder>(R.layout.sample_item_recycler) {

    override fun convert(holder: BaseViewHolder, item: ArticleDetailBean?) {
        holder.setText(R.id.item_textView, item?.title)
    }
}