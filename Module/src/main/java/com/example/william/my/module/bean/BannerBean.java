package com.example.william.my.module.bean;

import androidx.annotation.Keep;

import com.example.william.my.core.network.base.BaseBean;
import com.google.gson.annotations.SerializedName;

@Keep
public class BannerBean extends BaseBean {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("desc")
    public String desc;
    @SerializedName("imagePath")
    public String imagePath;

    public BannerData convert() {
        BannerData bannerData = new BannerData();
        bannerData.id = id;
        bannerData.title = title;
        bannerData.desc = desc;
        bannerData.image = imagePath;
        return bannerData;
    }
}
