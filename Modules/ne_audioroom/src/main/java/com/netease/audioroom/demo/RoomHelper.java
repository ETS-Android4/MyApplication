package com.netease.audioroom.demo;

import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.yunxin.nertc.nertcvoiceroom.model.NERtcVoiceRoom;

public class RoomHelper {

    private NERtcVoiceRoom mNERtcVoiceRoom;

    /**
     * 直播间初始化
     *
     * @param neRtcVoiceRoom
     */
    public static void initRoom(NERtcVoiceRoom neRtcVoiceRoom) {

    }

    /**
     * 进入直播间
     */
    public void enterRoom(boolean anchorMode) {
        mNERtcVoiceRoom.enterRoom(anchorMode);
    }

    /**
     * 离开直播间
     */
    public void leaveRoom() {
        mNERtcVoiceRoom.leaveRoom();
    }

    /**
     * 静音
     */
    public void muteAudio() {
        boolean muted = mNERtcVoiceRoom.muteLocalAudio(!mNERtcVoiceRoom.isLocalAudioMute());
        if (muted) {
            ToastHelper.showToast("话筒已关闭");
        } else {
            ToastHelper.showToast("话筒已打开");
        }
    }
}
