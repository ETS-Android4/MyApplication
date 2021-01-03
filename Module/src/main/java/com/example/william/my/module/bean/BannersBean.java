package com.example.william.my.module.bean;

import com.example.william.my.core.network.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannersBean extends BaseBean {

    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("errorMsg")
    public String errorMsg;

    @SerializedName("data")
    public List<BannerBean> data;
}
