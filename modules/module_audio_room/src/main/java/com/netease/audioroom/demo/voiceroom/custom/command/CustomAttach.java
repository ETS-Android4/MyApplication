package com.netease.audioroom.demo.voiceroom.custom.command;

import com.netease.audioroom.demo.voiceroom.custom.CustomAttachParser;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONObject;

public abstract class CustomAttach implements MsgAttachment {

    protected int type;

    CustomAttach(int type) {
        this.type = type;
    }

    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }

    @Override
    public String toJson(boolean send) {
        return CustomAttachParser.packData(type, packData());
    }

    public int getType() {
        return type;
    }

    protected abstract void parseData(JSONObject data);

    protected abstract JSONObject packData();
}