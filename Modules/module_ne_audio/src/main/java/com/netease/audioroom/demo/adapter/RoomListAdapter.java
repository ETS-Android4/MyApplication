package com.netease.audioroom.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.yunxin.android.lib.picture.ImageLoader;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 直播间列表
 */
public class RoomListAdapter extends BaseQuickAdapter<VoiceRoomInfo, BaseViewHolder> {

    public RoomListAdapter() {
        super(R.layout.item_room_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, VoiceRoomInfo info) {
        ImageLoader.with(getContext()).load(info.getThumbnail()).error(R.drawable.chat_room_default_bg).into(holder.getView(R.id.iv_chat_room_bg));
        holder.setText(R.id.tv_chat_room_name,info.getName());
        holder.setText(R.id.tv_chat_room_member_num,getCurrentCount(info.getOnlineUserCount()));
        holder.setText(R.id.tv_chat_room_anchor_name,info.getNickname());
    }

    private String getCurrentCount(int count) {
        if (count < 10000) {
            return count + "人";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(count / 10000.f) + "w人";
    }
}
