package com.netease.audioroom.demo;

import android.content.Context;

import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.nertc.model.NERtcVoiceRoom;
import com.netease.yunxin.nertc.model.NERtcVoiceRoomDef;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomMessage;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.model.interfaces.Anchor;
import com.netease.yunxin.nertc.model.interfaces.Audience;
import com.netease.yunxin.nertc.model.interfaces.AudiencePlay;
import com.netease.yunxin.nertc.util.SuccessCallback;

import java.util.List;

public class ChatRoomHelper {

    private static NERtcVoiceRoom mNERtcVoiceRoom;

    public static NERtcVoiceRoom getNERtcVoiceRoom() {
        return mNERtcVoiceRoom;
    }

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

    /**
     * 进入直播间
     *
     * @param anchorMode 是否为主播
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
     * 发送消息
     */
    public static void sendMessage(String content) {
        mNERtcVoiceRoom.sendMessage(content);
    }

    /**
     * 本地是否静音
     */
    public static boolean isMicClosed() {
        return mNERtcVoiceRoom.isLocalAudioMute();
    }

    /**
     * 关闭话筒
     */
    public static void closeMic() {
        boolean muted = mNERtcVoiceRoom.muteLocalAudio(!isMicClosed());
        if (muted) {
            ToastHelper.showToast("话筒已关闭");
        } else {
            ToastHelper.showToast("话筒已打开");
        }
    }

    /**
     * 关闭扬声器
     */
    public static void closeAudio() {

    }

    private static Anchor anchor;

    public static void initAnchor(Anchor.Callback callback) {
        anchor = mNERtcVoiceRoom.getAnchor();
        anchor.setCallback(callback);
    }

    /**
     * 获取上麦请求列表
     */
    public static List<VoiceRoomSeat> getApplySeats() {
        return anchor.getApplySeats();
    }

    /**
     * 拒绝麦位申请
     */
    public static void denySeatApply(VoiceRoomSeat seat) {
        anchor.denySeatApply(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                VoiceRoomUser user = seat.getUser();
                String nick = user != null ? user.getNick() : "";
                ToastHelper.showToast("已拒绝“" + nick + "”的申请");
            }
        });
    }

    /**
     * 同意上麦申请
     *
     * @param seat
     */
    public static void approveSeatApply(VoiceRoomSeat seat) {
        boolean ret = anchor.approveSeatApply(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast("成功通过连麦请求");
            }
        });
        if (!ret) {
            denySeatApply(seat);
        }
    }

    /**
     * 踢人
     */
    public static void kickSeat(VoiceRoomSeat seat) {
        anchor.kickSeat(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                VoiceRoomUser user = seat.getUser();
                String nick = user != null ? user.getNick() : "";
                ToastHelper.showToast("已将“" + nick + "”踢下麦位");
            }
        });
    }

    /**
     * 关闭麦位
     */
    public static void closeSeat(VoiceRoomSeat seat) {
        anchor.closeSeat(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastHelper.showToast("麦位" + (seat.getIndex() + 1) + "已关闭");
            }
        });
    }

    /**
     * 屏蔽麦位
     */
    public static void muteSeat(VoiceRoomSeat seat) {
        anchor.muteSeat(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastHelper.showToast("该麦位语音已被屏蔽，无法发言");
            }
        });
    }

    /**
     * 打开麦位
     */
    public static void openSeat(VoiceRoomSeat seat) {
        String msg = "";
        String msgError = "";
        switch (seat.getStatus()) {
            case VoiceRoomSeat.Status.CLOSED:
                int position = seat.getIndex() + 1;
                msg = "“麦位" + position + "”已打开”";
                msgError = "“麦位" + position + "”打开失败”";
                break;
            case VoiceRoomSeat.Status.FORBID:
            case VoiceRoomSeat.Status.AUDIO_MUTED:
                msg = "“该麦位已“解除语音屏蔽”";
                msgError = "该麦位“解除语音屏蔽”失败";
                break;
            case VoiceRoomSeat.Status.AUDIO_CLOSED_AND_MUTED:
                msg = "该麦位已“解除语音屏蔽”";
                msgError = "该麦位“解除语音屏蔽”失败";
                break;
        }
        String text = msg;
        String textError = msgError;
        anchor.openSeat(seat, new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast(text);
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast(textError + " code " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast(textError + " " + throwable.getMessage());
            }
        });
    }

    private static Audience audience;

    public static void initAudience(Audience.Callback callback) {
        audience = mNERtcVoiceRoom.getAudience();
        audience.setCallback(callback);
        //注册播放器
        audience.getAudiencePlay().registerNotify(new AudiencePlay.PlayerNotify() {
            @Override
            public void onPreparing() {
            }

            @Override
            public void onPlaying() {
            }

            @Override
            public void onError() {
                if (Network.getInstance().isConnected()) {
                    ToastHelper.showToastLong("主播网络好像出了问题");
                }
            }
        });
    }

    /**
     * 上麦
     */
    public static void applySeat(VoiceRoomSeat seat) {
        audience.applySeat(seat, new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //onApplySeatSuccess();
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast("请求连麦失败 ， code = " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("请求连麦异常 ， e = " + throwable);
            }
        });
    }

    /**
     * 取消上麦
     */
    public static void cancelSeatApply() {
        audience.cancelSeatApply(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast("已取消申请上麦");
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast("操作失败");
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast("操作失败");
            }
        });
    }

    private static void leaveSeat() {
        audience.leaveSeat(new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast("您已下麦");
            }
        });
    }

    protected static VoiceRoomUser createUser() {
        AccountInfo accountInfo = DemoCache.getAccountInfo();
        return new VoiceRoomUser(accountInfo.account, accountInfo.nick, accountInfo.avatar);
    }
}
