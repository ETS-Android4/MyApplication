package com.example.william.my.core.keyvalue.ikv

import android.content.Context
import android.os.Parcelable

interface IKV {

    fun init(context: Context)

    fun putInt(key: String, value: Int)
    fun putDouble(key: String, value: Double)
    fun putString(key: String, value: String)
    fun putBoolean(key: String, value: Boolean)
    fun putFloat(key: String, value: Float)
    fun putLong(key: String, value: Long)
    fun putStringSet(key: String, value: Set<String>)
    fun putParcelable(key: String, value: Parcelable)

    fun getInt(key: String, default: Int): Int
    fun getDouble(key: String, default: Double): Double
    fun getString(key: String, default: String): String?
    fun getBoolean(key: String, default: Boolean): Boolean
    fun getFloat(key: String, default: Float): Float
    fun getLong(key: String, default: Long): Long
    fun getStringSet(key: String): HashSet<String>?
    fun <T : Parcelable?> getParcelable(key: String, cl: Class<T>?): T?
}