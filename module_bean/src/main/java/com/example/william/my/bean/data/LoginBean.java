package com.example.william.my.bean.data;

import com.google.gson.annotations.SerializedName;

public class LoginBean {

    @SerializedName("data")
    private LoginUserBean data;

    public LoginUserBean getUserData() {
        return data;
    }

    public void setUserData(LoginUserBean data) {
        this.data = data;
    }
}