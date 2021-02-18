package com.example.william.my.module.open.activity;

import android.util.Log;

import com.example.william.my.module.activity.BaseResponseActivity;
import com.tencent.mmkv.MMKV;

/**
 * https://github.com/Tencent/MMKV/wiki/android_tutorial_cn
 */
public class MMKVActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();
        String rootDir = MMKV.initialize(this);
        Log.e(TAG, "mmkv root: " + rootDir);

        MMKV kv = MMKV.defaultMMKV();

        if (kv != null) {
            kv.encode("bool", true);
            boolean bValue = kv.decodeBool("bool");

            kv.encode("int", Integer.MIN_VALUE);
            int iValue = kv.decodeInt("int");

            kv.encode("string", "Hello from mmkv");
            String str = kv.decodeString("string");
            showResponse(str);
        }
    }
}