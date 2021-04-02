package com.example.william.my.module.kotlin.data

data class LoginData(var user: User) {

    data class User(var id: String, var nickname: String)
}