package com.netease.audioroom.demo.act;

import android.app.Activity;

import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomInfo;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomMessage;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomSeat;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomUser;
import com.netease.yunxin.voiceroom.model.interfaces.Anchor;
import com.netease.yunxin.voiceroom.model.interfaces.Audience;
import com.netease.yunxin.voiceroom.model.interfaces.NERtcVoiceRoom;
import com.netease.yunxin.voiceroom.model.interfaces.NERtcVoiceRoomDef;

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

    private static NERtcVoiceRoom mNERtcVoiceRoom;

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
        NERtcVoiceRoom.setAccountMapper(new NERtcVoiceRoomDef.AccountMapper() {
            @Override
            public long accountToVoiceUid(String account) {
                return AccountInfo.accountUid(account);
            }
        });
        NERtcVoiceRoom.setMessageBuilder(new VoiceRoomMessage.MessageTextBuilder() {
            @Override
            public String roomEvent(String nick, boolean enter) {
                String who = "“" + nick + "”";
                String action = enter ? "进了房间" : "离开了房间";
                return who + action;
            }

            @Override
            public String seatEvent(VoiceRoomSeat seat, boolean enter) {
                VoiceRoomUser user = seat.getUser();
                String nick = user != null ? user.getNick() : "";
                String who = "“" + nick + "”";
                String action = enter ? "进入了麦位" : "退出了麦位";
                int position = seat.getIndex() + 1;
                return who + action + position;
            }
        });
        mNERtcVoiceRoom = NERtcVoiceRoom.sharedInstance(context);
        mNERtcVoiceRoom.init(BuildConfig.NERTC_APP_KEY, callback);
        mNERtcVoiceRoom.initRoom(roomInfo, createUser());
    }

    protected static VoiceRoomUser createUser() {
        AccountInfo accountInfo = DemoCache.getAccountInfo();
        return new VoiceRoomUser(accountInfo.account, accountInfo.nick, accountInfo.avatar);
    }

    /**
     * 进入房间
     *
     * @param anchorMode 是否为主播
     */
    public void enterRoom(boolean anchorMode) {

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

    //
    // =============================================================================================
    //

    public static void toggleApplySeat() {

    }

    public static void toggleLeaveSeat() {

    }

    public static void toggleCancelSeatApply() {

    }
}
