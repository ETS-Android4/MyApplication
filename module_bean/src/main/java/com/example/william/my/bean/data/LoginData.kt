package com.example.william.my.bean.data

import androidx.annotation.Keep

@Keep
data class LoginData(var data: User) {

    data class User(var id: String, var nickname: String)
}