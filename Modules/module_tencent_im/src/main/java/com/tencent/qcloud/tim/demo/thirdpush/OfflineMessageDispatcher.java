package com.tencent.qcloud.tim.demo.thirdpush;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;

/**
 * 离线消息
 */
public class OfflineMessageDispatcher {

    public static OfflineMessageBean parseOfflineMessage(Intent intent) {
        if (intent == null) {
            return null;
        }
        Bundle bundle = intent.getExtras();
        return null;
    }
}
