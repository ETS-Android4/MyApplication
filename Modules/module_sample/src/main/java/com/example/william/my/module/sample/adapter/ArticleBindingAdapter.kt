package com.example.william.my.module.sample.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.example.william.my.module.bean.ArticleDataBean
import com.example.william.my.module.sample.utils.response.KtRetrofitResponse

object ArticleBindingAdapter {

//    @JvmStatic
//    @BindingAdapter("items")
//    fun setItems(view: RecyclerView, articleBean: ArticleBean?) {
//        val adapter = view.adapter
//        if (adapter is ArticleDataBindAdapter) {
//            if (articleBean?.data != null) {
//                if (articleBean.data.datas.isNullOrEmpty()) {
//                    if (articleBean.data.curPage == 1) {
//                        ToastUtils.showShort("暂无数据")
//                    } else {
//                        ToastUtils.showShort("无更多数据")
//                    }
//                } else {
//                    if (articleBean.data.curPage == 1) {
//                        adapter.setNewInstance(articleBean.data.datas)
//                    } else {
//                        adapter.addData(articleBean.data.datas)
//                    }
//                }
//            }
//        } else {
//            throw IllegalArgumentException("RecyclerView.Adapter is not ArticleBindAdapter")
//        }
//    }

    @JvmStatic
    @BindingAdapter("items")
    fun setItemsResponse(view: RecyclerView, articleBean: KtRetrofitResponse<ArticleDataBean>?) {
        val adapter = view.adapter
        if (adapter is ArticleDataBindAdapter) {
            if (articleBean?.data != null) {
                if (articleBean.data.datas.isNullOrEmpty()) {
                    if (articleBean.data.curPage == 1) {
                        ToastUtils.showShort("暂无数据")
                    } else {
                        ToastUtils.showShort("无更多数据")
                    }
                } else {
                    if (articleBean.data.curPage == 1) {
                        adapter.setNewInstance(articleBean.data.datas)
                    } else {
                        adapter.addData(articleBean.data.datas)
                    }
                }
            }
        } else {
            throw IllegalArgumentException("RecyclerView.Adapter is not ArticleBindAdapter")
        }
    }
}