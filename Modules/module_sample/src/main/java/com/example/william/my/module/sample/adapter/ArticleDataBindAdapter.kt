package com.example.william.my.module.sample.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.william.my.module.bean.ArticleDetailBean
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleItemBindRecyclerBinding

class ArticleDataBindAdapter :
    BaseQuickAdapter<ArticleDetailBean?, ArticleDataBindAdapter.DataBindingHolder>(R.layout.sample_item_bind_recycler) {

    override fun convert(holder: DataBindingHolder, item: ArticleDetailBean?) {
        //item?.run {
        //    holder.itemBind?.itemTextView?.text = title
        //    holder.itemBind?.executePendingBindings()//防止列表闪烁
        //}
        holder.itemBind?.data = item
        holder.itemBind?.executePendingBindings()//防止列表闪烁
    }

    class DataBindingHolder(view: View) :
        BaseDataBindingHolder<SampleItemBindRecyclerBinding>(view) {
        var itemBind: SampleItemBindRecyclerBinding? = dataBinding
    }
}