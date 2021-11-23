package com.netease.audioroom.demo.act;

import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomMessage;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;

import java.util.List;

public interface IChatRoomCallback {

    /**
     * 进入房间
     * 进入聊天室和语音通道
     *
     * @param success 是否成功
     */
    void onEnterRoom(boolean success);

    /**
     * 离开房间
     */
    void onLeaveRoom();

    /**
     * 房间被解散
     */
    void onRoomDismiss();

    /**
     * 主播信息更新
     *
     * @param user {@link VoiceRoomSeat 资料信息}
     */
    void onAnchorInfo(VoiceRoomUser user);

    /**
     * 主播静音状态
     *
     * @param muted 是否静音
     */
    void onAnchorMute(boolean muted);

    /**
     * 主播说话音量
     *
     * @param volume 说话音量0-100
     */
    void onAnchorVolume(int volume);

    /**
     * 当前在线用户数量更新
     *
     * @param onlineUserCount 当前在线用户数量
     */
    void onOnlineUserCount(int onlineUserCount);

    /**
     * 是否关闭麦克风
     *
     * @param muted 是否静音
     */
    void onLocalMuteMic(boolean muted);

    /**
     * 是否关闭音频
     *
     * @param muted
     */
    void onRoomMuteAudio(boolean muted);

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
    void onUpdateVolume(int index, int volume);

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
