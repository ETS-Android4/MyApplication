package com.example.william.my.module.kotlin.utils

import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.example.william.my.library.base.BaseApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

object DataStoreUtils {

    private val dataStore by lazy {
        BaseApp.getApp().createDataStore(
            name = "settings"
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun <U> getData(key: String, default: U): Flow<U> {
        val data = when (default) {
            is Long -> readDataFlow(longPreferencesKey(key), default)
            is String -> readDataFlow(stringPreferencesKey(key), default)
            is Int -> readDataFlow(intPreferencesKey(key), default)
            is Boolean -> readDataFlow(booleanPreferencesKey(key), default)
            is Float -> readDataFlow(floatPreferencesKey(key), default)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
        return data as Flow<U>
    }

    @Suppress("UNCHECKED_CAST")
    fun <U> getSyncData(key: String, default: U): U {
        val res = when (default) {
            is Long -> readData(longPreferencesKey(key), default)
            is String -> readData(stringPreferencesKey(key), default)
            is Int -> readData(intPreferencesKey(key), default)
            is Boolean -> readData(booleanPreferencesKey(key), default)
            is Float -> readData(floatPreferencesKey(key), default)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
        return res as U
    }

    private fun <U> readDataFlow(preferences: Preferences.Key<U>, default: U): Flow<U> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferences] ?: default
            }

    private fun <U> readData(preferences: Preferences.Key<U>, default: U): U? {
        var value: U = default
        runBlocking {
            dataStore.data.first {
                value = it[preferences] ?: default
                true
            }
        }
        return value
    }

    suspend fun <U> putData(key: String, value: U) {
        when (value) {
            is Long -> saveData(longPreferencesKey(key), value)
            is String -> saveData(stringPreferencesKey(key), value)
            is Int -> saveData(intPreferencesKey(key), value)
            is Boolean -> saveData(booleanPreferencesKey(key), value)
            is Float -> saveData(floatPreferencesKey(key), value)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    fun <U> putSyncData(key: String, value: U) {
        when (value) {
            is Long -> saveSyncData(longPreferencesKey(key), value)
            is String -> saveSyncData(stringPreferencesKey(key), value)
            is Int -> saveSyncData(intPreferencesKey(key), value)
            is Boolean -> saveSyncData(booleanPreferencesKey(key), value)
            is Float -> saveSyncData(floatPreferencesKey(key), value)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    private suspend fun <U> saveData(preferences: Preferences.Key<U>, value: U) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferences] = value
        }
    }

    private fun <U> saveSyncData(preferences: Preferences.Key<U>, value: U) =
        runBlocking { saveData(preferences, value) }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    fun clearSync() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}