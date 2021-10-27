package com.netease.audioroom.demo.voiceroom.custom.command;

import com.netease.audioroom.demo.voiceroom.custom.CustomAttachParser;

import org.json.JSONObject;

/**
 * 推流重新开始的自定义消息
 */
public class StreamRestartedAttach extends CustomAttach {

    public StreamRestartedAttach() {
        super(CustomAttachParser.CustomAttachType.STREAM_RESTARTED);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
