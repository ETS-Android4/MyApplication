package com.netease.yunxin.nertc.model.interfaces;

import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;

/**
 * Created by luc on 1/19/21.
 */
public interface PushTypeSwitcher {

    void toCDN(String url);

    void toRTC(VoiceRoomInfo roomInfo, long uid);
}
