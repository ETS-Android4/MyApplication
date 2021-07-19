package com.netease.yunxin.nertc.model;

import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;

import java.util.List;

public abstract class NERtcVoiceRoomInner extends NERtcVoiceRoom {

    public abstract void updateSeat(VoiceRoomSeat seat);

    public abstract VoiceRoomSeat getSeat(int index);

    public abstract void sendSeatEvent(VoiceRoomSeat seat, boolean enter);

    public abstract void sendSeatUpdate(VoiceRoomSeat seat, RequestCallback<Void> callback);

    public abstract void fetchRoomSeats(final RequestCallback<List<VoiceRoomSeat>> callback);

    public abstract void refreshSeats();

    public abstract boolean isInitial();
}
