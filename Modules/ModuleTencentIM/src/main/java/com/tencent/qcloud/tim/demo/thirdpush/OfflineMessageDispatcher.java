package com.tencent.qcloud.tim.demo.thirdpush;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;

public class OfflineMessageDispatcher {

    private static final String TAG = OfflineMessageDispatcher.class.getSimpleName();

    public static OfflineMessageBean parseOfflineMessage(Intent intent) {
        DemoLog.i(TAG, "intent: " + intent);
        if (intent == null) {
            return null;
        }
        Bundle bundle = intent.getExtras();
        DemoLog.i(TAG, "bundle: " + bundle);
        return null;
    }
}
