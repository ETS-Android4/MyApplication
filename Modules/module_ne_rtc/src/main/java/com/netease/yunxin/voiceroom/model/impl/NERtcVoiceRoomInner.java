package com.netease.yunxin.voiceroom.model.impl;

import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomSeat;
import com.netease.yunxin.voiceroom.model.interfaces.NERtcVoiceRoom;

import java.util.List;

/**
 * 直播间（麦位）实现功能
 */
public abstract class NERtcVoiceRoomInner extends NERtcVoiceRoom {

    public abstract void updateSeat(VoiceRoomSeat seat);

    public abstract VoiceRoomSeat getSeat(int index);

    public abstract void sendSeatEvent(VoiceRoomSeat seat, boolean enter);

    public abstract void sendSeatUpdate(VoiceRoomSeat seat, RequestCallback<Void> callback);

    public abstract void sendSeatUpdateList(List<VoiceRoomSeat> seats, RequestCallback<List<String>> callback);

    public abstract void fetchRoomSeats(final RequestCallback<List<VoiceRoomSeat>> callback);

    public abstract void refreshSeats();

    public abstract boolean isInitial();
}
