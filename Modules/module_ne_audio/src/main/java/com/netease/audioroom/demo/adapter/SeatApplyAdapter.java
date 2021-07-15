package com.netease.audioroom.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.adapter.BaseAdapter;
import com.netease.audioroom.demo.widget.HeadImageView;
import com.netease.yunxin.android.lib.picture.ImageLoader;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

import java.util.ArrayList;

/**
 * 上麦列表
 */
public class SeatApplyAdapter extends BaseAdapter<VoiceRoomSeat> {

    public SeatApplyAdapter(Context context) {
        super(new ArrayList<>(), context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ApplyViewHolder(layoutInflater.inflate(R.layout.apply_item_layout, parent, false));
    }

    @Override
    protected void onBindBaseViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VoiceRoomSeat seat = getItem(position);
        if (seat == null) {
            return;
        }
        ApplyViewHolder viewHolder = (ApplyViewHolder) holder;
        VoiceRoomUser user = seat.getUser();
        if (user != null) {
            int index = seat.getIndex() + 1;
            ImageLoader.with(context).load(user.getAvatar()).error(R.drawable.nim_avatar_default).into(viewHolder.ivAvatar);
            viewHolder.tvContent.setText(user.getNick() + "\t申请麦位(" + index + ")");
            viewHolder.ivRefuse.setOnClickListener((v) -> applyAction.refuse(seat));
            viewHolder.ivAfree.setOnClickListener((v) -> applyAction.agree(seat));
        } else {
            Log.e("TAG", "偶现看不到申请者情形");
        }
    }

    private static class ApplyViewHolder extends RecyclerView.ViewHolder {
        HeadImageView ivAvatar;
        ImageView ivRefuse;
        ImageView ivAfree;
        TextView tvContent;

        public ApplyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.item_requestlink_headicon);
            ivRefuse = itemView.findViewById(R.id.refuse);
            ivAfree = itemView.findViewById(R.id.agree);
            tvContent = itemView.findViewById(R.id.item_requestlink_content);
        }
    }

    public interface IApplyAction {
        void refuse(VoiceRoomSeat seat);

        void agree(VoiceRoomSeat seat);
    }

    IApplyAction applyAction;

    public void setApplyAction(IApplyAction applyAction) {
        this.applyAction = applyAction;
    }
}
