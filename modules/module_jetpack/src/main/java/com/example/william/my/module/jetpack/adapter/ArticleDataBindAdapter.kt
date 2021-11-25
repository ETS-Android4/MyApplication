package com.example.william.my.module.jetpack.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.william.my.bean.data.ArticleDetailBean

import com.example.william.my.module.jetpack.R
import com.example.william.my.module.jetpack.databinding.SampleItemRecyclerBindBinding

class ArticleDataBindAdapter :
    BaseQuickAdapter<ArticleDetailBean?, ArticleDataBindAdapter.DataBindingHolder>(R.layout.sample_item_recycler_bind) {

    override fun convert(holder: DataBindingHolder, item: ArticleDetailBean?) {
        //item?.run {
        //    holder.itemBind?.itemTextView?.text = title
        //    holder.itemBind?.executePendingBindings()//防止列表闪烁
        //}
        holder.itemBind?.data = item
        holder.itemBind?.executePendingBindings()//防止列表闪烁
    }

    class DataBindingHolder(view: View) :
        BaseDataBindingHolder<SampleItemRecyclerBindBinding>(view) {
        var itemBind: SampleItemRecyclerBindBinding? = dataBinding
    }
}
