package com.example.william.my.module.demo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleBean {

    @SerializedName("curPage")
    private int curPage;
    @SerializedName("size")
    private int size;

    @SerializedName("datas")
    private List<ArticleDetailBean> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ArticleDetailBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ArticleDetailBean> datas) {
        this.datas = datas;
    }

}
