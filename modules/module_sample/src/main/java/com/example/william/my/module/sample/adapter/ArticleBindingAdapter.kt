package com.example.william.my.module.sample.adapter

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.core.retrofit.response.RetrofitResponse


object ArticleBindingAdapter {

    @JvmStatic
    @BindingAdapter("items")
    fun setItemsResponse(
        view: RecyclerView,
        articleBean: RetrofitResponse<ArticleDataBean>?
    ) {
        val adapter = view.adapter
        if (adapter is ArticleDataBindAdapter) {
            if (articleBean?.data != null) {
                if (!articleBean.data.datas.isNullOrEmpty()) {
                    showArticles(adapter, articleBean.data.curPage == 1, articleBean.data.datas)
                } else {
                    onDataNotAvailable(view.context, adapter, articleBean.data.curPage == 1)
                }
            }
        } else {
            throw IllegalArgumentException("RecyclerView.Adapter is not ArticleBindAdapter")
        }
    }

    private fun showArticles(
        adapter: ArticleDataBindAdapter,
        isFirst: Boolean,
        datas: MutableList<ArticleDetailBean?>
    ) {
        if (isFirst) {
            adapter.setNewInstance(datas)
        } else {
            adapter.addData(datas)
        }
    }

    private fun onDataNotAvailable(
        context: Context,
        adapter: ArticleDataBindAdapter,
        isFirst: Boolean
    ) {
        if (isFirst) {
            showEmptyView(context, adapter)
        } else {
            onDataNoMore()
        }
    }

    private fun showEmptyView(context: Context, adapter: ArticleDataBindAdapter) {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = "无数据"
        adapter.setEmptyView(textView)
        adapter.notifyDataSetChanged()
    }

    private fun onDataNoMore() {
        ToastUtils.showShort("无更多数据")
    }
}