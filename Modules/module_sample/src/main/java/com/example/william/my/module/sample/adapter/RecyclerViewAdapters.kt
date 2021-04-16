package com.example.william.my.module.sample.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.module.bean.ArticleDetailBean

object RecyclerViewAdapters {

    @BindingAdapter("items")
    fun setItems(view: RecyclerView, items: MutableList<ArticleDetailBean?>) {
        val adapter = view.adapter
        if (adapter is ArticleBindAdapter) {
            adapter.setNewInstance(items)
            // tell RestaurantRecyclerViewAdapter to set new list of items
        } else {
            throw IllegalArgumentException("RecyclerView.Adapter is not ArticleBindAdapter")
        }
    }
}