package com.example.william.my.kotlin.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExamplePreferenceDataStore(context: Context) {

    //创建 DataStore
    private val dataStore by lazy {
        context.createDataStore(
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

    suspend fun incrementCounter() {
        dataStore.edit {
            //it[EXAMPLE_COUNTER] = counter
            val currentCounterValue = it[EXAMPLE_COUNTER] ?: 0
            it[EXAMPLE_COUNTER] = currentCounterValue + 1
        }
    }

    companion object {
        val EXAMPLE_COUNTER = preferencesKey<Int>("example_counter")
    }
}