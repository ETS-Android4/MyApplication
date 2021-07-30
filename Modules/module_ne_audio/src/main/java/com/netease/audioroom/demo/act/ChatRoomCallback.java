package com.netease.audioroom.demo.act;

import com.netease.audioroom.demo.databinding.ActivityAnchorBinding;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomMessage;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomSeat;

import java.util.List;

public class ChatRoomCallback implements IChatRoomCallback {

    public ActivityAnchorBinding mBinding;

    public ChatRoomCallback(ActivityAnchorBinding binding) {
        this.mBinding = binding;
    }

    @Override
    public void onUpdateSeats(List<VoiceRoomSeat> seats) {

    }

    @Override
    public void onUpdateSeat(VoiceRoomSeat seat) {

    }

    @Override
    public void onApplySeats(List<VoiceRoomSeat> seats) {

    }

    @Override
    public void updateVolume(int index, int volume) {

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

    @Override
    public void onVoiceRoomMessage(VoiceRoomMessage message) {

    }
}