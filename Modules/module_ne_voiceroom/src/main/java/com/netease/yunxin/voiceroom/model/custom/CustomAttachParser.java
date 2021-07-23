package com.netease.yunxin.voiceroom.model.custom;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;
import com.netease.yunxin.voiceroom.model.custom.command.CloseRoomAttach;
import com.netease.yunxin.voiceroom.model.custom.command.CustomAttach;
import com.netease.yunxin.voiceroom.model.custom.command.StreamRestartedAttach;
import com.netease.yunxin.voiceroom.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "type";

    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {
        CustomAttach attachment = null;
        JSONObject object = JsonUtil.parse(json);
        if (object == null) {
            return null;
        }
        int type = object.optInt(KEY_TYPE);
        JSONObject data = object.optJSONObject(KEY_DATA);
        switch (type) {
            case CustomAttachType.CLOSER_ROOM:
                attachment = new CloseRoomAttach();
                break;
            case CustomAttachType.STREAM_RESTARTED:
                attachment = new StreamRestartedAttach();
                break;
        }

        if (attachment != null) {
            attachment.fromJson(data);
        }
        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        try {
            object.put(KEY_TYPE, type);
            if (data != null) {
                object.put(KEY_DATA, data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public interface CustomAttachType {

        int CLOSER_ROOM = 1; // 关闭房间

        int STREAM_RESTARTED = 2;// 推流重新开始

    }
}
