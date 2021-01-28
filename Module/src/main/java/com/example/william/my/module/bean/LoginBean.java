package com.example.william.my.module.bean;

import com.google.gson.annotations.SerializedName;

public class LoginBean {

    @SerializedName("data")
    private UserBean data;

    public UserBean getUserData() {
        return data;
    }

    public void setUserData(UserBean data) {
        this.data = data;
    }

    public static class UserBean {

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
}
