package com.netease.audioroom.demo;

import com.netease.yunxin.nertc.model.bean.VoiceRoomMessage;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;

import java.util.List;

public interface IChatRoomCallback {
    /**
     * 更新所有麦位信息
     *
     * @param seats {@link VoiceRoomSeat 麦位}
     */
    void onUpdateSeats(List<VoiceRoomSeat> seats);

    /**
     * 更新麦位信息
     *
     * @param seat {@link VoiceRoomSeat 麦位}
     */
    void onUpdateSeat(VoiceRoomSeat seat);

    /**
     * 收到消息
     *
     * @param message {@link VoiceRoomMessage 消息}
     */
    void onVoiceRoomMessage(VoiceRoomMessage message);
}
