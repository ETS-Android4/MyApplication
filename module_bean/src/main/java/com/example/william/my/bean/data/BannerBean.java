package com.example.william.my.bean.data;

import com.example.william.my.core.retrofit.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerBean extends BaseBean {

    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("errorMsg")
    public String errorMsg;

    @SerializedName("data")
    public List<BannerDetailBean> data;
}
