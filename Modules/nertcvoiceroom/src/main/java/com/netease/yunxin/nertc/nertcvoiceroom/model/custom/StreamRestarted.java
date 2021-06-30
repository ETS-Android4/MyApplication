package com.netease.yunxin.nertc.nertcvoiceroom.model.custom;

import org.json.JSONObject;

public class StreamRestarted extends CustomAttachment{

    public StreamRestarted() {
        super(CustomAttachmentType.STREAM_RESTARTED);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
