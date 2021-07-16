package com.netease.audioroom.demo.act;

import com.netease.audioroom.demo.IChatRoomCallback;
import com.netease.audioroom.demo.databinding.ActivityAnchorBinding;
import com.netease.yunxin.nertc.model.bean.VoiceRoomMessage;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;

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
    public void onVoiceRoomMessage(VoiceRoomMessage message) {

    }
}