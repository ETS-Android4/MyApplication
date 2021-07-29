package com.netease.audioroom.demo.act;

import com.netease.yunxin.voiceroom.model.bean.VoiceRoomMessage;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomSeat;

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
     * 更新上麦请求列表
     *
     * @param seats {@link VoiceRoomSeat 麦位}
     */
    void onApplySeats(List<VoiceRoomSeat> seats);

    /**
     * 更新音量
     */
    void updateVolume(int index, int volume);

    /**
     * 麦位被屏蔽语音
     */
    void onSeatMuted();

    /**
     * 麦位被关闭
     */
    void onSeatClosed();

    /**
     * 是否被禁言（发送文字消息）
     */
    void onTextMuted(boolean muted);

    /**
     * 收到消息
     *
     * @param message {@link VoiceRoomMessage 消息}
     */
    void onVoiceRoomMessage(VoiceRoomMessage message);
}
