package com.example.william.my.bean.data

import androidx.annotation.Keep

@Keep
data class LoginBean(var data: User = User()) {

    data class User(var id: String = "", var nickname: String = "")
}