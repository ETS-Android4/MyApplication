package com.netease.audioroom.demo;

import android.app.Activity;

import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.yunxin.nertc.model.NERtcVoiceRoomDef;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomMessage;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.model.interfaces.Anchor;
import com.netease.yunxin.nertc.model.interfaces.Audience;

import java.util.List;

/**
 * 直播间管理单例
 */
public class ChatRoomManager implements NERtcVoiceRoomDef.RoomCallback, Anchor.Callback, Audience.Callback {

    private static final ChatRoomManager instance = new ChatRoomManager();

    private ChatRoomManager() {

    }

    public static ChatRoomManager getInstance() {
        return instance;
    }

    private IChatRoomCallback mIChatRoomCallback;

    /**
     * 绑定Activity
     */
    public void onActivityAttach(IChatRoomCallback iChatRoomCallback) {
        mIChatRoomCallback = iChatRoomCallback;
    }

    /**
     * 解绑Activity
     */
    public void onActivityDetach() {
        this.mIChatRoomCallback = null;
    }

    /**
     * 初始化房间
     */
    public void initRoom(Activity context, VoiceRoomInfo roomInfo, NERtcVoiceRoomDef.RoomCallback callback) {
        ChatRoomHelper.initRoom(context, roomInfo, this);
    }

    /**
     * 进入房间
     *
     * @param anchorMode 是否为主播
     */
    public void enterRoom(boolean anchorMode) {
        if (!ChatHelper.isLogin()) {
            ChatHelper.loginIM(new ChatLoginManager.Callback() {
                @Override
                public void onSuccess(AccountInfo accountInfo) {
                    ChatRoomHelper.enterRoom(anchorMode);
                }

                @Override
                public void onFailed(int code, String errorMsg) {

                }
            });
        } else {
            ChatRoomHelper.enterRoom(anchorMode);
        }
    }

    @Override
    public void onEnterRoom(boolean success) {

    }

    @Override
    public void onLeaveRoom() {

    }

    @Override
    public void onRoomDismiss() {

    }

    @Override
    public void onAnchorInfo(VoiceRoomUser user) {

    }

    @Override
    public void onAnchorMute(boolean muted) {

    }

    @Override
    public void onAnchorVolume(int volume) {

    }

    @Override
    public void onOnlineUserCount(int onlineUserCount) {

    }

    @Override
    public void onUpdateSeats(List<VoiceRoomSeat> seats) {

    }

    @Override
    public void onUpdateSeat(VoiceRoomSeat seat) {

    }

    @Override
    public void onSeatVolume(VoiceRoomSeat seat, int volume) {

    }

    @Override
    public void onVoiceRoomMessage(VoiceRoomMessage message) {

    }

    @Override
    public void onLocalMuteMic(boolean muted) {

    }

    @Override
    public void onRoomMuteAudio(boolean muted) {

    }

    @Override
    public void onApplySeats(List<VoiceRoomSeat> seats) {

    }

    @Override
    public void onSeatApplyDenied(boolean otherOn) {

    }

    @Override
    public void onEnterSeat(VoiceRoomSeat seat, boolean last) {

    }

    @Override
    public void onLeaveSeat(VoiceRoomSeat seat, boolean bySelf) {

    }

    @Override
    public void onSeatMuted() {

    }

    @Override
    public void onSeatClosed() {

    }

    @Override
    public void onTextMuted(boolean muted) {

    }
}
