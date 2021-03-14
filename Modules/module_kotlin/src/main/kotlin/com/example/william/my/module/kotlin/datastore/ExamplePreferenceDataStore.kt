package com.example.william.my.module.kotlin.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExamplePreferenceDataStore(context: Context) {

    //创建 DataStore
    private val dataStore by lazy {
        context.createDataStore(
            //migrations = listOf(SharedPreferencesMigration(context, "sp")),
            name = "settings"
        )
    }

    fun getCounter(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[EXAMPLE_COUNTER] ?: 0
            }
    }

//    fun getCounterFirst(): Flow<Int> {
//        return dataStore.data.first
//            .map { preferences ->
//                // No type safety.
//                preferences[EXAMPLE_COUNTER] ?: 0
//                true
//            }
//    }

    suspend fun incrementCounter() {
        dataStore.edit {
            //it[EXAMPLE_COUNTER] = counter
            val currentCounterValue = it[EXAMPLE_COUNTER] ?: 0
            it[EXAMPLE_COUNTER] = currentCounterValue + 1
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        val EXAMPLE_COUNTER = intPreferencesKey("example_counter")
    }
}