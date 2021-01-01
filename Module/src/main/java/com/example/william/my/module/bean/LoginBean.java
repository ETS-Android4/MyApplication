package com.example.william.my.module.bean;

import com.example.william.my.core.network.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class LoginBean extends BaseBean {

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends BaseBean {

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
}
