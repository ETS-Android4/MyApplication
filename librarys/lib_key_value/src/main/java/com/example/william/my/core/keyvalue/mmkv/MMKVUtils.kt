package com.example.william.my.core.keyvalue.mmkv

import android.content.Context
import android.os.Parcelable
import com.example.william.my.core.keyvalue.ikv.IKV
import com.tencent.mmkv.MMKV

/**
 * https://github.com/Tencent/MMKV/wiki/android_tutorial_cn
 */
class MMKVUtils : IKV {

    private lateinit var kv: MMKV

    override fun init(context: Context) {
        MMKV.initialize(context)
        kv = MMKV.defaultMMKV()
    }

    override fun putInt(key: String, value: Int) {
        kv.encode(key, value)
    }

    override fun putDouble(key: String, value: Double) {
        kv.encode(key, value)
    }

    override fun putString(key: String, value: String) {
        kv.encode(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        kv.encode(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        kv.encode(key, value)
    }

    override fun putLong(key: String, value: Long) {
        kv.encode(key, value)
    }

    override fun putStringSet(key: String, value: Set<String>) {
        kv.encode(key, value)
    }

    override fun putParcelable(key: String, value: Parcelable) {
        kv.encode(key, value)
    }

    override fun getInt(key: String, default: Int): Int {
        return kv.decodeInt(key, default)
    }

    override fun getDouble(key: String, default: Double): Double {
        return kv.decodeDouble(key, default)
    }

    override fun getString(key: String, default: String): String? {
        return kv.decodeString(key, default)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return kv.decodeBool(key, default)
    }

    override fun getFloat(key: String, default: Float): Float {
        return kv.decodeFloat(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return kv.decodeLong(key, default)
    }

    override fun getStringSet(key: String): HashSet<String>? {
        return kv.decodeStringSet(key) as HashSet<String>?
    }

    override fun <T : Parcelable?> getParcelable(key: String, cl: Class<T>?): T? {
        return kv.decodeParcelable(key, cl)
    }
}

