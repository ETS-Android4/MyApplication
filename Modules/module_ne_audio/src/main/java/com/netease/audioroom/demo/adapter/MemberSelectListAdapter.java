package com.netease.audioroom.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.widget.OnMenuItemClickListener;
import com.netease.yunxin.android.lib.picture.ImageLoader;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择成员列表
 */
public class MemberSelectListAdapter extends RecyclerView.Adapter<MemberSelectListAdapter.ChatRoomHolder> {

    private static final int TYPE_EMPTY = 1;
    private static final int TYPE_NORMAL = 2;

    private final List<VoiceRoomUser> members = new ArrayList<>();
    private final Context context;
    private OnMenuItemClickListener<Integer> onMenuItemClickListener;

    public MemberSelectListAdapter(List<VoiceRoomUser> dataList, Context context) {
        if (dataList != null && !dataList.isEmpty()) {
            this.members.addAll(dataList);
        }
        this.context = context;
    }

    public MemberSelectListAdapter(Context context) {
        this(null, context);
    }

    public void updateDataSource(List<VoiceRoomUser> members) {
        if (members == null) {
            return;
        }
        this.members.clear();
        this.members.addAll(members);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnMenuItemClickListener<Integer> onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public int getItemCount() {
        return members.isEmpty() ? 1 : members.size();
    }

    @NonNull
    @Override
    public ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new ChatRoomHolder(LayoutInflater.from(context).inflate(R.layout.view_item_dialog_members_empty, parent, false), false);
        }
        return new ChatRoomHolder(LayoutInflater.from(context).inflate(R.layout.item_base_recycle, parent, false), true);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {
        if (getItemViewType(position) == TYPE_EMPTY) {
            return;
        }
        VoiceRoomUser member = members.get(position);
        if (member == null) {
            return;
        }
        ImageLoader.with(context).commonLoad(member.getAvatar(), holder.ivBg);
        holder.tvRoomName.setText(member.getNick());
        holder.itemView.setOnClickListener(v -> {
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return members.isEmpty() ? TYPE_EMPTY : TYPE_NORMAL;
    }

    public static class ChatRoomHolder extends RecyclerView.ViewHolder {
        ImageView ivBg;
        TextView tvRoomName;

        ChatRoomHolder(View itemView, boolean needInit) {
            super(itemView);
            if (!needInit) {
                ((TextView) (itemView.findViewById(R.id.tv_empty_comment))).setText("暂无群成员");
                return;
            }
            ivBg = itemView.findViewById(R.id.iv_head);
            tvRoomName = itemView.findViewById(R.id.tv_text);

        }
    }
}