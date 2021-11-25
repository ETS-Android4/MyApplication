package com.example.william.my.module.sample.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class LoginBean(val data: User) : Parcelable {

    @Keep
    @Parcelize
    class User(val id: String, val nickname: String) : Parcelable
}