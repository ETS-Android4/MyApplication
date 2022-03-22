package com.example.william.my.module.sample.databinding

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.module.sample.adapter.ArticleBindAdapter

/**
 * DataBinding
 * https://developer.android.google.cn/topic/libraries/data-binding/binding-adapters
 */
object ArticleBindingAdapter {

    @JvmStatic
    @BindingAdapter("items")
    fun setItemsResponse(view: RecyclerView, articleResponse: RetrofitResponse<ArticleDataBean>) {
        val adapter = view.adapter
        if (adapter is ArticleBindAdapter) {
            articleResponse.data?.let { article ->
                if (article.datas.isNullOrEmpty()) {
                    onDataNotAvailable(view.context, adapter, article.curPage == 1)
                } else {
                    showArticles(adapter, article.curPage == 1, article.datas)
                }
            }
        } else {
            throw IllegalArgumentException("RecyclerView.Adapter is not ArticleBindAdapter")
        }
    }

    private fun showArticles(adapter: ArticleBindAdapter, isFirst: Boolean, datas: MutableList<ArticleDetailBean?>) {
        if (isFirst) {
            adapter.setNewInstance(datas)
        } else {
            adapter.addData(datas)
        }
    }

    private fun onDataNotAvailable(context: Context, adapter: ArticleBindAdapter, isFirst: Boolean) {
        if (isFirst) {
            showEmptyView(context, adapter)
        }
    }

    private fun showEmptyView(context: Context, adapter: ArticleBindAdapter) {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = "无数据"
        adapter.setEmptyView(textView)
    }
}