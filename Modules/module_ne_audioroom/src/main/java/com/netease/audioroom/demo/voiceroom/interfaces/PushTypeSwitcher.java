package com.netease.audioroom.demo.voiceroom.interfaces;

import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;

/**
 * Created by luc on 1/19/21.
 */
public interface PushTypeSwitcher {

    void toCDN(String url);

    void toRTC(VoiceRoomInfo roomInfo, long uid);
}
