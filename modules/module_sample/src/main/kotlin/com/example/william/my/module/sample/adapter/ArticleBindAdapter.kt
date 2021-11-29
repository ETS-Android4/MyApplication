package com.example.william.my.module.sample.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleItemDataBingBinding

class ArticleBindAdapter : BaseQuickAdapter<ArticleDetailBean?, ArticleBindAdapter.DataBindingHolder>(R.layout.sample_item_data_bing) {

    override fun convert(holder: DataBindingHolder, item: ArticleDetailBean?) {
        //item?.run {
        //    holder.itemBind?.itemTextView?.text = title
        //    holder.itemBind?.executePendingBindings()//防止列表闪烁
        //}
        holder.itemBind?.data = item
        holder.itemBind?.executePendingBindings()//防止列表闪烁
    }

    class DataBindingHolder(view: View) : BaseDataBindingHolder<SampleItemDataBingBinding>(view) {
        var itemBind: SampleItemDataBingBinding? = dataBinding
    }
}
