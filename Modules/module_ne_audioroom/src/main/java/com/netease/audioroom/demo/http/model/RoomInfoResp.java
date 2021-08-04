package com.netease.audioroom.demo.http.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.netease.audioroom.demo.voiceroom.bean.StreamConfig;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;

import java.util.ArrayList;

/**
 * Created by luc on 12/24/20.
 */
public class RoomInfoResp {

    @SerializedName("total")
    public int total;

    @SerializedName("list")
    public ArrayList<RoomInfoItem> roomInfoItems;

    public ArrayList<VoiceRoomInfo> toVoiceRoomInfoList() {
        if (roomInfoItems == null || roomInfoItems.isEmpty()) {
            return new ArrayList<>(0);
        }
        ArrayList<VoiceRoomInfo> result = new ArrayList<>(roomInfoItems.size());
        for (RoomInfoItem item : roomInfoItems) {
            if (item != null) {
                result.add(item.toVoiceRoomInfo());
            }
        }
        return result;
    }

    public static class RoomInfoItem {

        @SerializedName("roomId")
        String roomId;

        @SerializedName("creator")
        String creatorAccount;

        @SerializedName("name")
        String name;

        @SerializedName("thumbnail")
        String thumbnail;

        @SerializedName("nickname")
        String nickname;

        @SerializedName("onlineUserCount")
        int onlineUserCount;

        @SerializedName("liveConfig")
        String configStr;

        @SerializedName("roomType")
        int roomType;


        public VoiceRoomInfo toVoiceRoomInfo() {
            VoiceRoomInfo roomInfo = new VoiceRoomInfo();
            roomInfo.setRoomId(roomId);
            roomInfo.setCreatorAccount(creatorAccount);
            roomInfo.setRoomType(roomType);
            roomInfo.setName(name);
            roomInfo.setThumbnail(thumbnail);
            roomInfo.setNickname(nickname);
            roomInfo.setOnlineUserCount(onlineUserCount);
            if (!TextUtils.isEmpty(configStr)) {
                PushConfig config = new Gson().fromJson(configStr, PushConfig.class);
                roomInfo.setStreamConfig(
                        new StreamConfig(config.pushUrl, config.httpPullUrl, config.rtmpPullUrl, config.hlsPullUrl));
            }
            return roomInfo;
        }
    }

    public static class PushConfig {

        @SerializedName("pushUrl")
        String pushUrl;

        @SerializedName("httpPullUrl")
        String httpPullUrl;

        @SerializedName("rtmpPullUrl")
        String rtmpPullUrl;

        @SerializedName("hlsPullUrl")
        String hlsPullUrl;
    }
}
