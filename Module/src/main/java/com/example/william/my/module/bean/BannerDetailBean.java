package com.example.william.my.module.bean;

import androidx.annotation.Keep;

import com.example.william.my.core.network.base.BaseBean;
import com.google.gson.annotations.SerializedName;

@Keep
public class BannerDetailBean extends BaseBean {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("desc")
    public String desc;
    @SerializedName("imagePath")
    public String imagePath;

    public BannerDetailData convert() {
        BannerDetailData bannerDetailData = new BannerDetailData();
        bannerDetailData.id = id;
        bannerDetailData.title = title;
        bannerDetailData.desc = desc;
        bannerDetailData.image = imagePath;
        return bannerDetailData;
    }
}