package com.netease.yunxin.nertc.nertcvoiceroom.model;

import com.netease.yunxin.nertc.nertcvoiceroom.model.bean.VoiceRoomInfo;

/**
 * Created by luc on 1/19/21.
 */
public interface PushTypeSwitcher {

    void toCDN(String url);

    void toRTC(VoiceRoomInfo roomInfo, long uid);
}
