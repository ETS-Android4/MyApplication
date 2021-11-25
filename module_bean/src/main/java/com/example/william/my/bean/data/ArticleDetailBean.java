package com.example.william.my.bean.data;

import com.example.william.my.core.retrofit.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class ArticleDetailBean extends BaseBean {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("link")
    private String link;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}