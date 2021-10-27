package com.example.william.my.core.keyvalue.mmkv

import android.content.Context
import android.os.Parcelable
import com.example.william.my.core.keyvalue.ikv.IKV
import com.tencent.mmkv.MMKV

/**
 * https://github.com/Tencent/MMKV/wiki/android_tutorial_cn
 */
object MMKVUtils : IKV {

    override fun init(context: Context) {
        MMKV.initialize(context)
    }

    override fun putInt(key: String, value: Int) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun putDouble(key: String, value: Double) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun putString(key: String, value: String) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun putLong(key: String, value: Long) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun putStringSet(key: String, value: Set<String>) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun putParcelable(key: String, value: Parcelable) {
        val kv = MMKV.defaultMMKV()
        kv.encode(key, value)
    }

    override fun getInt(key: String, default: Int): Int {
        val kv = MMKV.defaultMMKV()
        return kv.decodeInt(key, default)
    }

    override fun getDouble(key: String, default: Double): Double {
        val kv = MMKV.defaultMMKV()
        return kv.decodeDouble(key, default)
    }

    override fun getString(key: String, default: String): String {
        val kv = MMKV.defaultMMKV()
        return kv.decodeString(key, default) ?: default
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        val kv = MMKV.defaultMMKV()
        return kv.decodeBool(key, default)
    }

    override fun getFloat(key: String, default: Float): Float {
        val kv = MMKV.defaultMMKV()
        return kv.decodeFloat(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        val kv = MMKV.defaultMMKV()
        return kv.decodeLong(key, default)
    }

    override fun getStringSet(key: String): Set<String> {
        val kv = MMKV.defaultMMKV()
        return kv.decodeStringSet(key) ?: HashSet()
    }

    override fun <T : Parcelable?> getParcelable(key: String, cl: Class<T>): T? {
        val kv = MMKV.defaultMMKV()
        return kv.decodeParcelable(key, cl)
    }

    override fun clear() {
        val kv = MMKV.defaultMMKV()
        kv.clearAll()
    }
}

