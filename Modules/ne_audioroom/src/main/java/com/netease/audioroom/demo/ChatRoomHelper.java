package com.netease.audioroom.demo;

import android.content.Context;

import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.yunxin.nertc.model.NERtcVoiceRoom;
import com.netease.yunxin.nertc.model.NERtcVoiceRoomDef;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomMessage;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

public class ChatRoomHelper {

    private static NERtcVoiceRoom mNERtcVoiceRoom;

    /**
     * 直播间初始化
     */
    public static void initRoom(Context context, VoiceRoomInfo roomInfo, NERtcVoiceRoomDef.RoomCallback callback) {
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

    public static void joinRoom(boolean anchorMode) {
        if (LoginManager.isLogin()) {
            enterRoom(anchorMode);
        } else {
            ChatHelper.loginIM(new LoginManager.Callback() {
                @Override
                public void onSuccess(AccountInfo accountInfo) {
                    enterRoom(anchorMode);
                }

                @Override
                public void onFailed(int code, String errorMsg) {
                    ToastHelper.showToast("加入房间失败");
                }
            });
        }
    }

    /**
     * 进入直播间
     */
    public static void enterRoom(boolean anchorMode) {
        mNERtcVoiceRoom.enterRoom(anchorMode);
    }

    /**
     * 离开直播间
     */
    public static void leaveRoom() {
        mNERtcVoiceRoom.leaveRoom();
    }

    /**
     * 静音
     */
    public static void muteAudio() {
        boolean muted = mNERtcVoiceRoom.muteLocalAudio(!mNERtcVoiceRoom.isLocalAudioMute());
        if (muted) {
            ToastHelper.showToast("话筒已关闭");
        } else {
            ToastHelper.showToast("话筒已打开");
        }
    }

    /**
     * 发送消息
     */
    public static void sendMessage(String content) {
        mNERtcVoiceRoom.sendMessage(content);
    }

    protected static VoiceRoomUser createUser() {
        AccountInfo accountInfo = DemoCache.getAccountInfo();
        return new VoiceRoomUser(accountInfo.account, accountInfo.nick, accountInfo.avatar);
    }
}
