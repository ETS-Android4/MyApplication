package com.netease.audioroom.demo.act;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomMessage;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;
import com.netease.audioroom.demo.voiceroom.interfaces.Anchor;
import com.netease.audioroom.demo.voiceroom.interfaces.Audience;
import com.netease.audioroom.demo.voiceroom.interfaces.NERtcVoiceRoom;
import com.netease.audioroom.demo.voiceroom.interfaces.NERtcVoiceRoomDef;

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

    private Activity mActivity;
    private boolean isAnchorMode;
    private VoiceRoomInfo mVoiceRoomInfo;

    private Anchor mAnchor;
    private Audience mAudience;
    private NERtcVoiceRoom mNERtcVoiceRoom;

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
    public void initRoom(Activity context, VoiceRoomInfo roomInfo) {
        this.mActivity = context;
        this.mVoiceRoomInfo = roomInfo;
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
        mNERtcVoiceRoom.init(BuildConfig.NERTC_APP_KEY, this);
        mNERtcVoiceRoom.initRoom(roomInfo, new VoiceRoomUser(
                DemoCache.getAccountInfo().account,
                DemoCache.getAccountInfo().nick,
                DemoCache.getAccountInfo().avatar));
    }

    /**
     * 进入房间
     *
     * @param anchorMode 是否为主播
     */
    public void enterRoom(boolean anchorMode) {
        this.isAnchorMode = anchorMode;
        this.mAudience = mNERtcVoiceRoom.getAudience();
        mNERtcVoiceRoom.enterRoom(anchorMode);
    }

    //
    // ===
    //

    @Override
    public void onEnterRoom(boolean success) {
        mIChatRoomCallback.onEnterRoom(success);
    }

    @Override
    public void onLeaveRoom() {
        mIChatRoomCallback.onLeaveRoom();
    }

    @Override
    public void onRoomDismiss() {
        mIChatRoomCallback.onRoomDismiss();
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
        mIChatRoomCallback.onOnlineUserCount(onlineUserCount);
    }

    @Override
    public void onUpdateSeats(List<VoiceRoomSeat> seats) {
        mIChatRoomCallback.onUpdateSeats(seats);
    }

    @Override
    public void onUpdateSeat(VoiceRoomSeat seat) {
        mIChatRoomCallback.onUpdateSeat(seat);
    }

    @Override
    public void onSeatVolume(VoiceRoomSeat seat, int volume) {

    }

    @Override
    public void onVoiceRoomMessage(VoiceRoomMessage message) {

    }

    @Override
    public void onLocalMuteMic(boolean muted) {
        mIChatRoomCallback.onLocalMuteMic(muted);
    }

    @Override
    public void onRoomMuteAudio(boolean muted) {
        mIChatRoomCallback.onRoomMuteAudio(muted);
    }

    //
    // ===
    //

    @Override
    public void onApplySeats(List<VoiceRoomSeat> seats) {

    }

    //
    // ===
    //

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

    /**
     * 是否上麦状态
     */
    public boolean isInChannel() {
        VoiceRoomSeat seat = mAudience.getSeat();
        return seat != null && seat.isOn();
    }

    /**
     * 离开房间
     */
    public void toggleLeaveRoom() {
        if (isAnchorMode) {
            mNERtcVoiceRoom.leaveRoom();
        } else if (!mVoiceRoomInfo.isSupportCDN()) {
            mNERtcVoiceRoom.leaveRoom();
        } else if (isInChannel()) {
            mNERtcVoiceRoom.leaveRoom();
        } else {
            mActivity.finish();
        }
    }

    /**
     * 麦克风是否静音
     */
    public boolean isMicClosed() {
        return mNERtcVoiceRoom.isLocalMicMute();
    }

    /**
     * 关闭麦克风
     */
    public void toggleCloseLocalMic() {
        boolean muted = mNERtcVoiceRoom.muteLocalMic(!isMicClosed());
        if (muted) {
            ToastUtils.showShort("话筒已关闭");
        } else {
            ToastUtils.showShort("话筒已打开");
        }
    }

    /**
     * 房间是否静音
     */
    public boolean isRoomAudioMute() {
        return mNERtcVoiceRoom.isRoomAudioMute();
    }

    /**
     * 关闭扬声器
     */
    public void toggleCloseRoomAudio() {
        boolean muted = mNERtcVoiceRoom.muteRoomAudio(!isRoomAudioMute());
        if (muted) {
            ToastUtils.showShort("已关闭“聊天室声音”");
        } else {
            ToastUtils.showShort("已打开“聊天室声音”");
        }
    }

    public void toggleApplySeat() {

    }

    public void toggleLeaveSeat() {

    }

    public void toggleCancelSeatApply() {

    }

    public void toggleSendMessage(String content) {
        mNERtcVoiceRoom.sendMessage(content);
    }

}
