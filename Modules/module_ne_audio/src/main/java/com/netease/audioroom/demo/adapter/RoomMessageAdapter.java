package com.netease.audioroom.demo.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.widget.MessageSpannableStr;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomMessage;

/**
 * 消息列表
 */
public class RoomMessageAdapter extends BaseQuickAdapter<VoiceRoomMessage, BaseViewHolder> {

    public RoomMessageAdapter() {
        super(R.layout.item_msg_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, @NonNull VoiceRoomMessage message) {
        if (message.type == VoiceRoomMessage.Type.TEXT) {
            CharSequence content = new MessageSpannableStr.Builder()
                    .append(message.nick + "：")
                    .append(message.content)
                    .build().getMessageInfo();
            holder.setText(R.id.tv_chat_content, content);
        } else if (message.type == VoiceRoomMessage.Type.EVENT) {
            holder.setText(R.id.tv_chat_content, message.content);
        }
    }
}
