package com.netease.yunxin.voiceroom.model.custom.command;

import com.netease.yunxin.voiceroom.model.custom.CustomAttachParser;

import org.json.JSONObject;

/**
 * 解散聊天室的自定义消息
 */
public class CloseRoomAttach extends CustomAttach {

    public CloseRoomAttach() {
        super(CustomAttachParser.CustomAttachType.CLOSER_ROOM);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
