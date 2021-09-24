package com.example.william.my.core.keyvalue

import com.example.william.my.core.keyvalue.datastore.DataStoreUtils
import com.example.william.my.core.keyvalue.ikv.IKV

object KeyValue {

    val INSTANCE: IKV
        get() {
            //return MMKVUtils.getInstance()
            return DataStoreUtils.getInstance()
        }
}