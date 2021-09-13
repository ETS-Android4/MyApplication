package com.example.william.my.core.keyvalue.datastore

import android.content.Context
import android.os.Parcelable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.createDataStore
import com.example.william.my.core.keyvalue.ikv.IKV
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * https://developer.android.google.cn/topic/libraries/architecture/datastore
 */
class DataStoreUtils : IKV {

    private lateinit var ds: DataStore<Preferences>

    override fun init(context: Context) {
        ds = context.createDataStore(
            name = "settings"
        )
    }

    override fun putInt(key: String, value: Int) {
        putData(intPreferencesKey(key), value)
    }

    override fun putDouble(key: String, value: Double) {
        putData(doublePreferencesKey(key), value)
    }

    override fun putString(key: String, value: String) {
        putData(stringPreferencesKey(key), value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        putData(booleanPreferencesKey(key), value)
    }

    override fun putFloat(key: String, value: Float) {
        putData(floatPreferencesKey(key), value)
    }

    override fun putLong(key: String, value: Long) {
        putData(longPreferencesKey(key), value)
    }

    override fun putStringSet(key: String, value: Set<String>) {
        putData(stringSetPreferencesKey(key), value)
    }

    override fun putParcelable(key: String, value: Parcelable) {

    }

    override fun getInt(key: String, default: Int): Int {
        return getData(intPreferencesKey(key), default) ?: default
    }

    override fun getDouble(key: String, default: Double): Double {
        return getData(doublePreferencesKey(key), default) ?: default
    }

    override fun getString(key: String, default: String): String? {
        return getData(stringPreferencesKey(key), default)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return getData(booleanPreferencesKey(key), default) ?: default
    }

    override fun getFloat(key: String, default: Float): Float {
        return getData(floatPreferencesKey(key), default) ?: default
    }

    override fun getLong(key: String, default: Long): Long {
        return getData(longPreferencesKey(key), default) ?: default
    }

    override fun getStringSet(key: String): HashSet<String> {
        return getData(stringSetPreferencesKey(key), HashSet()) as HashSet<String>
    }

    override fun <T : Parcelable?> getParcelable(key: String, cl: Class<T>?): T? {
        return null
    }

    private suspend fun <U> writeData(key: Key<U>, value: U) {
        ds.edit { settings ->
            settings[key] = value
        }
    }

    private fun <U> putData(key: Key<U>, value: U) {
        return runBlocking {
            writeData(key, value)
        }
    }

    private fun <U> readData(key: Key<U>, default: U): Flow<U> {
        return ds.data
            .map {
                it[key] ?: default
            }
    }

    private fun <U> getData(key: Key<U>, default: U): U? {
        var value: U = default
        runBlocking {
            readData(key, default).map {
                value = it
            }
        }
        return value
    }
}