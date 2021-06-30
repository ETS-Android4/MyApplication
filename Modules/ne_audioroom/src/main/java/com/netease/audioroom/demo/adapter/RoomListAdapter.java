package com.netease.audioroom.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.adapter.BaseAdapter;
import com.netease.yunxin.android.lib.picture.ImageLoader;
import com.netease.yunxin.nertc.nertcvoiceroom.model.bean.VoiceRoomInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播间列表
 */
public class RoomListAdapter extends BaseAdapter<VoiceRoomInfo> {

    public RoomListAdapter(Context context) {
        super(new ArrayList<>(), context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ChatRoomHolder(layoutInflater.inflate(R.layout.item_chat_room_list, parent, false));
    }

    @Override
    protected void onBindBaseViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRoomHolder viewHolder = (ChatRoomHolder) holder;
        VoiceRoomInfo info = getItem(position);
        if (info == null) {
            return;
        }
        ImageLoader.with(context).load(info.getThumbnail()).error(R.drawable.chat_room_default_bg).into(viewHolder.ivBg);
        viewHolder.tvRoomName.setText(info.getName());
        viewHolder.tvMember.setText(getCurrentCount(info.getOnlineUserCount()));
        viewHolder.tvAnchorName.setText(info.getNickname());
    }

    public void refreshList(List<VoiceRoomInfo> dataList) {
        if (dataList == null) {
            return;
        }
        setItems(dataList);
        notifyDataSetChanged();
    }

    private String getCurrentCount(int count) {
        if (count < 10000) {
            return count + "人";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(count / 10000.f) + "w人";
    }

    private static class ChatRoomHolder extends RecyclerView.ViewHolder {

        ImageView ivBg;

        TextView tvRoomName;

        TextView tvMember;

        TextView tvAnchorName;

        ChatRoomHolder(View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.iv_chat_room_bg);
            tvRoomName = itemView.findViewById(R.id.tv_chat_room_name);
            tvMember = itemView.findViewById(R.id.tv_chat_room_member_num);
            tvAnchorName = itemView.findViewById(R.id.tv_chat_room_anchor_name);
        }
    }
}
