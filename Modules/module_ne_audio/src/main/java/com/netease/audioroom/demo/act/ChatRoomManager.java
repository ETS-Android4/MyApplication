package com.netease.audioroom.demo.act;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.activity.BaseRoomActivity;
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

    private Activity mActivity;
    private VoiceRoomInfo mVoiceRoomInfo;

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
        mNERtcVoiceRoom.enterRoom(anchorMode);
    }

    @Override
    public void onEnterRoom(boolean success) {
        if (!success) {
            ToastUtils.showShort("进入聊天室失败");
            mActivity.finish();
        }
    }

    @Override
    public void onLeaveRoom() {
        mActivity.finish();
    }

    @Override
    public void onRoomDismiss() {
        new AlertDialog.Builder(mActivity)
                .setTitle("通知")
                .setMessage("该房间已被主播解散")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChatRoomHelper.leaveRoom();
                        if (mVoiceRoomInfo.isSupportCDN()) {
                            mActivity.finish();
                        }
                    }
                })
                .setCancelable(false)
                .create()
                .show();
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

    public void toggleLeaveRoom() {
        mNERtcVoiceRoom.leaveRoom();
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
