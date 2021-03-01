package com.example.william.my.module.kotlin.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class LoginData(val data: User) : Parcelable {

    @Keep
    @Parcelize
    class User(val id: String, val nickname: String) : Parcelable
}