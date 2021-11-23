package com.netease.audioroom.demo.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.yunxin.android.lib.picture.ImageLoader;

import java.text.DecimalFormat;

/**
 * 直播间列表
 */
public class RoomListAdapter extends BaseQuickAdapter<VoiceRoomInfo, BaseViewHolder> {

    public RoomListAdapter() {
        super(R.layout.item_room_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, @NonNull VoiceRoomInfo info) {
        ImageLoader.with(getContext()).load(info.getThumbnail()).error(R.drawable.chat_room_default_bg).into(holder.getView(R.id.iv_chat_room_bg));
        holder.setText(R.id.tv_room_name, info.getName());
        holder.setText(R.id.tv_chat_room_member_num, getCurrentCount(info.getOnlineUserCount()));
        holder.setText(R.id.tv_chat_room_anchor_name, info.getNickname());
    }

    private String getCurrentCount(int count) {
        if (count < 10000) {
            return count + "人";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(count / 10000.f) + "w人";
    }
}
