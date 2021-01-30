package com.example.william.my.kotlin.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.example.william.my.kotlin.proto.SettingsProto.Settings
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class ExampleProtoDataStore(context: Context) {

    //创建 DataStore
    private val dataStore by lazy {
        context.createDataStore(
            fileName = "settings.pb",
            serializer = SettingsSerializer
        )
    }

    fun getCounter(): Flow<Int> {
        return dataStore.data
            .map { settings ->
                // The exampleCounter property is generated from the proto schema.
                settings.exampleCounter
            }
    }

    suspend fun incrementCounter() {
        dataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setExampleCounter(currentSettings.exampleCounter + 1)
                .build()
        }
    }

    suspend fun clear() {
        dataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }
    }

    object SettingsSerializer : Serializer<Settings> {

        override val defaultValue: Settings = Settings.getDefaultInstance()

        override fun readFrom(input: InputStream): Settings {
            try {
                return Settings.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override fun writeTo(
            t: Settings,
            output: OutputStream
        ) = t.writeTo(output)
    }
}