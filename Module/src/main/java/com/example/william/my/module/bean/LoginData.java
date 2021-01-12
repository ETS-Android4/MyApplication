package com.example.william.my.module.bean;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class LoginData {

    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }
}
