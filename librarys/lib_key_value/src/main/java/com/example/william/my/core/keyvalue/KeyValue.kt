package com.example.william.my.core.keyvalue

import com.example.william.my.core.keyvalue.datastore.DataStoreUtils
import com.example.william.my.core.keyvalue.ikv.IKV
import com.example.william.my.core.keyvalue.mmkv.MMKVUtils

object KeyValue {

    val INSTANCE: IKV
        get() {
            //return MMKVUtils()
            return DataStoreUtils()
        }
}