package com.example.william.my.module.sample.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.william.my.retrofit.ArticleDetailBean
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleItemRecyclerBindBinding

class ArticleDataBindAdapter :
    BaseQuickAdapter<com.example.william.my.retrofit.ArticleDetailBean?, ArticleDataBindAdapter.DataBindingHolder>(R.layout.sample_item_recycler_bind) {

    override fun convert(holder: DataBindingHolder, item: com.example.william.my.retrofit.ArticleDetailBean?) {
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
