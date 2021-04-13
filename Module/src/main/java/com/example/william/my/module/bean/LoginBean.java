package com.example.william.my.module.bean;

import com.example.william.my.core.network.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class LoginBean extends BaseBean {

    @SerializedName("data")
    private LoginUserBean data;

    public LoginUserBean getUserData() {
        return data;
    }

    public void setUserData(LoginUserBean data) {
        this.data = data;
    }
}
