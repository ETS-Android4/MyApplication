package com.netease.audioroom.demo.voiceroom.impl;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.netease.audioroom.demo.voiceroom.SeatCommands;
import com.netease.audioroom.demo.voiceroom.SeatStatusHelper;
import com.netease.audioroom.demo.voiceroom.bean.StreamConfig;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat.Reason;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat.Status;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;
import com.netease.audioroom.demo.voiceroom.callback.DoneCallback;
import com.netease.audioroom.demo.voiceroom.callback.RequestCallbackEx;
import com.netease.audioroom.demo.voiceroom.interfaces.Audience;
import com.netease.audioroom.demo.voiceroom.interfaces.AudiencePlay;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

import java.util.List;

/**
 * 观众实现类
 */
public class AudienceImpl implements Audience {

    private final NERtcVoiceRoomInner voiceRoom;

    /**
     * 消息服务
     */
    private final MsgService msgService;

    /**
     * 房间信息
     */
    private VoiceRoomInfo voiceRoomInfo;

    /**
     * 资料信息
     */
    private VoiceRoomUser user;

    /**
     * 当前麦位
     */
    private VoiceRoomSeat mySeat;

    /**
     * 观众回调
     */
    private Callback callback;

    /**
     * cdn 模式下播放器控制
     */
    @Deprecated
    private final AudiencePlay audiencePlay = new AudiencePlayImpl();

    private final SeatStatusHelper statusRecorder;

    public AudienceImpl(NERtcVoiceRoomImpl voiceRoom) {
        this.voiceRoom = voiceRoom;
        this.statusRecorder = new SeatStatusHelper(voiceRoom);
        this.msgService = NIMClient.getService(MsgService.class);
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void applySeat(final VoiceRoomSeat seat, final RequestCallback<Void> callback) {
        if (mySeat != null && (mySeat.isOn() || mySeat.getStatus() == Status.APPLY)) {
            if (callback != null) {
                callback.onFailed(-1);
            }
            return;
        }
        VoiceRoomSeat backup = seat.getBackup();
        backup.apply();
        backup.setUser(user);
        statusRecorder.updateSeat(backup, new SeatStatusHelper.ExecuteAction() {
            @Override
            public void onSuccess() {
                doApplySeat(seat, callback);
            }

            @Override
            public void onFail() {
                if (callback != null) {
                    callback.onFailed(-1);
                }
            }
        });

    }

    private void doApplySeat(VoiceRoomSeat seat, final RequestCallback<Void> callback) {
        mySeat = seat;

        sendNotification(SeatCommands.applySeat(voiceRoomInfo, user, seat), new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                if (mySeat == null) {
                    return;
                }

                VoiceRoomSeat q = voiceRoom.getSeat(mySeat.getIndex());
                if (q.getStatus() == Status.CLOSED) {
                    mySeat.setStatus(Status.CLOSED);
                    voiceRoom.updateSeat(mySeat);
                    return;
                }
                mySeat.setStatus(Status.APPLY);

                if (callback != null) {
                    callback.onSuccess(param);
                }
            }

            @Override
            public void onFailed(int code) {
                if (callback != null) {
                    callback.onFailed(code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (callback != null) {
                    callback.onException(exception);
                }
            }
        });
    }

    @Override
    public void cancelSeatApply(final RequestCallback<Void> callback) {
        if (mySeat == null) {
            return;
        }
        VoiceRoomSeat backup = mySeat.getBackup();
        backup.cancelApply();
        backup.setUser(user);
        statusRecorder.updateSeat(backup, new SeatStatusHelper.ExecuteAction() {
            @Override
            public void onSuccess() {
                final VoiceRoomSeat seat = mySeat;
                if (seat == null || seat.getStatus() == Status.CLOSED) {
                    return;
                }
                seat.setReason(Reason.CANCEL_APPLY);
                sendNotification(SeatCommands.cancelSeatApply(voiceRoomInfo, user, seat), new RequestCallbackEx<Void>(callback) {
                    @Override
                    public void onSuccess(Void param) {
                        if (mySeat != null && mySeat.getReason() == Reason.CANCEL_APPLY) {
                            seat.cancelApply();
                            mySeat = null;
                        }

                        super.onSuccess(param);
                    }
                });
            }

            @Override
            public void onFail() {
                if (callback != null) {
                    callback.onFailed(-1);
                }
            }
        });

    }

    @Override
    public void leaveSeat(final RequestCallback<Void> callback) {
        if (mySeat == null) {
            return;
        }
        mySeat.setReason(Reason.LEAVE);
        sendNotification(SeatCommands.leaveSeat(voiceRoomInfo, user, mySeat), new RequestCallbackEx<Void>(callback) {
            @Override
            public void onSuccess(Void param) {
                onLeaveSeat(mySeat, true);
                mySeat = null;
                super.onSuccess(param);
            }
        });
    }

    @Override
    public void fetchSeats(RequestCallback<List<VoiceRoomSeat>> callback) {
        voiceRoom.fetchRoomSeats(callback);
    }

    @Override
    public VoiceRoomSeat getSeat() {
        return mySeat != null ? voiceRoom.getSeat(mySeat.getIndex()) : null;
    }

    @Deprecated
    @Override
    public AudiencePlay getAudiencePlay() {
        return audiencePlay;
    }

    @Override
    public void refreshSeat() {
        voiceRoom.refreshSeats();
    }

    // wifi 2 4G  if enter room async delay, may be npe
    @Deprecated
    @Override
    public void restartAudioOrNot() {
        if (voiceRoomInfo == null) {
            return;
        }
        StreamConfig config = voiceRoomInfo.getStreamConfig();
        if (config == null) {
            return;
        }
        if (TextUtils.isEmpty(config.rtmpPullUrl)) {
            return;
        }
        VoiceRoomSeat seat = getSeat();
        if (seat != null && seat.isOn()) {
            return;
        }
        getAudiencePlay().play(config.rtmpPullUrl);
    }

    public void enterRoom(VoiceRoomInfo voiceRoomInfo, VoiceRoomUser user, EnterChatRoomResultData result) {
        this.voiceRoomInfo = voiceRoomInfo;
        this.user = user;
        clearSeats();
        ChatRoomMember member = result.getMember();
        ChatRoomInfo roomInfo = result.getRoomInfo();
        if (roomInfo.isMute() || member.isTempMuted() || member.isMuted()) {
            muteText(true);
        }
    }

    public boolean leaveRoom(Runnable runnable) {
        if (!audiencePlay.isReleased()) {
            audiencePlay.release();
        }
        if (mySeat == null) {
            Log.e("AudienceImpl", "leaveRoom seat is null.");
            return false;
        }
        leaveSeat(new DoneCallback<Void>(runnable));
        return true;
    }

    public void updateRoomInfo(ChatRoomInfo roomInfo) {
        if (roomInfo.isMute()) {
            muteText(true);
        }
    }

    public void updateMemberInfo(@NonNull ChatRoomMember member) {
        if (!member.isTempMuted() && !member.isMuted()) {
            muteText(false);
        }
    }

    public void memberExit(String account) {

    }

    public void initSeats(@NonNull List<VoiceRoomSeat> seats) {
        List<VoiceRoomSeat> userSeats = VoiceRoomSeat.find(seats, user.getAccount());
        for (VoiceRoomSeat seat : userSeats) {
            if (seat != null && seat.isOn()) {
                mySeat = seat;
                onEnterSeat(seat, true);
                break;
            }
        }
    }

    public void clearSeats() {
        mySeat = null;
    }

    public void seatChange(VoiceRoomSeat seat) {
        // my seat is 'STATUS_CLOSE'
        if (seat.getStatus() == Status.CLOSED
                && mySeat != null && mySeat.isSameIndex(seat)) {
            mySeat = null;
            if (callback != null) {
                callback.onSeatClosed();
            }
            return;
        }

        // others
        if (!seat.isSameAccount(user.getAccount())) {
            // my seat is 'STATUS_NORMAL' by other
            if (seat.getStatus() == Status.ON
                    && mySeat != null && mySeat.isSameIndex(seat)) {
                mySeat = null;
                if (callback != null) {
                    callback.onSeatApplyDenied(true);
                }
            }
        } else {
            switch (seat.getStatus()) {
                case Status.ON:
                    mySeat = seat;
                    onEnterSeat(seat, false);
                    break;
                case Status.AUDIO_MUTED:
                    mySeat = seat;
                    muteSeat();
                    break;
                case Status.INIT:
                case Status.FORBID:
                    if (seat.getReason() == Reason.ANCHOR_DENY_APPLY) {
                        if (callback != null) {
                            callback.onSeatApplyDenied(false);
                        }
                    } else if (seat.getReason() == Reason.ANCHOR_KICK) {
                        onLeaveSeat(seat, false);
                    }
                    mySeat = null;
                    break;
                case Status.CLOSED:
                    if (mySeat != null && mySeat.getStatus() == Status.APPLY) {
                        if (callback != null) {
                            callback.onSeatApplyDenied(false);
                        }
                    } else {
                        if (seat.getReason() == Reason.ANCHOR_KICK) {
                            onLeaveSeat(seat, false);
                        }
                    }
                    mySeat = null;
                    break;
                case Status.AUDIO_CLOSED:
                case Status.AUDIO_CLOSED_AND_MUTED:
                    mySeat = seat;
                    break;
            }
        }
    }

    public void muteLocalAudio(boolean muted) {
        if (mySeat == null) {
            return;
        }
        mySeat.muteSelf(muted);
        voiceRoom.sendSeatUpdate(mySeat, null);
    }

    public void muteText(boolean mute) {
        if (callback != null) {
            callback.onTextMuted(mute);
        }
    }

    private void onEnterSeat(VoiceRoomSeat seat, boolean last) {
        mySeat = seat;
        if (voiceRoomInfo.isSupportCDN()) {
            voiceRoom.getPushTypeSwitcher().toRTC(voiceRoomInfo, Long.parseLong(user.getAccount()));
        }
        voiceRoom.startLocalAudio();
        if (voiceRoom.isLocalMicMute()) {
            voiceRoom.muteLocalMic(false);
        }
        if (callback != null) {
            callback.onEnterSeat(seat, last);
        }
    }

    private void onLeaveSeat(VoiceRoomSeat seat, boolean bySelf) {
        if (voiceRoomInfo.isSupportCDN() && voiceRoom.isInitial()) {
            voiceRoom.getPushTypeSwitcher().toCDN(voiceRoomInfo.getStreamConfig().httpPullUrl);
        }

        voiceRoom.enableEarBack(false);
        voiceRoom.stopLocalAudio();
        if (callback != null) {
            callback.onLeaveSeat(seat, bySelf);
        }
    }

    private void muteSeat() {
        voiceRoom.stopLocalAudio();
        if (callback != null) {
            callback.onSeatMuted();
        }
    }

    private void sendNotification(CustomNotification notification, RequestCallback<Void> callback) {
        if (notification == null) {
            if (callback != null) {
                callback.onException(null);
            }
            return;
        }
        notification.setSendToOnlineUserOnly(false);
        msgService.sendCustomNotification(notification).setCallback(callback);
    }
}
