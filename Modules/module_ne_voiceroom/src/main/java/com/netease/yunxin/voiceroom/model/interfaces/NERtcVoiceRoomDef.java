package com.netease.yunxin.voiceroom.model.interfaces;

import com.netease.yunxin.voiceroom.model.bean.VoiceRoomMessage;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomSeat;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomUser;

import java.util.List;

/**
 * 语聊房定义
 */
public class NERtcVoiceRoomDef {

    /**
     * 房间语音质量
     */
    public interface RoomAudioQuality {
        /**
         * 默认音质
         */
        int DEFAULT_QUALITY = 0;

        /**
         * 高音质
         */
        int HIGH_QUALITY = 1;
    }

    /**
     * 房间回调
     */
    public interface RoomCallback {
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
         * 麦位音量
         *
         * @param seat   {@link VoiceRoomSeat 麦位}
         * @param volume 说话音量0-100
         */
        void onSeatVolume(VoiceRoomSeat seat, int volume);

        /**
         * 收到消息
         *
         * @param message {@link VoiceRoomMessage 消息}
         */
        void onVoiceRoomMessage(VoiceRoomMessage message);

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

    }

    /**
     * 账号映射接口
     */
    public interface AccountMapper {
        /**
         * 为账号分配唯一的数值id
         *
         * @param account
         * @return uid
         */
        long accountToVoiceUid(String account);
    }
}
