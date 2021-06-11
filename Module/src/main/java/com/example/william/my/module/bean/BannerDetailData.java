package com.example.william.my.module.bean;

import androidx.annotation.Keep;

import com.example.william.my.core.retrofit.base.BaseBean;
import com.google.gson.annotations.SerializedName;

@Keep
public class BannerDetailData extends BaseBean {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("desc")
    public String desc;
    @SerializedName("imagePath")
    public String image;
}
