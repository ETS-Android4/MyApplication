package com.example.william.my.retrofit.bean;

import com.google.gson.annotations.SerializedName;

public class LoginUserBean {

    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}