package com.netease.audioroom.demo.adapter;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.yunxin.android.lib.picture.ImageLoader;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomSeat;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomUser;

public class BaseRecycleAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseRecycleAdapter() {
        super(R.layout.item_room_member);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, T t) {
        ImageView ivAccount = holder.findView(R.id.iv_account);
        TextView tvContent = holder.findView(R.id.tv_content);
        if (tvContent != null && ivAccount != null) {
            if (t instanceof String) {
                //菜单
                tvContent.setGravity(Gravity.CENTER);
                tvContent.setText(t.toString());
            } else if (t instanceof VoiceRoomUser) {
                //成员列表，禁言列表
                VoiceRoomUser member = (VoiceRoomUser) t;
                ivAccount.setVisibility(View.VISIBLE);
                ImageLoader.with(getContext()).commonLoad(member.getAvatar(), ivAccount);
                holder.setText(R.id.tv_content, member.getNick());
            } else if (t instanceof VoiceRoomSeat) {
                //申请上麦列表
                VoiceRoomSeat seat = (VoiceRoomSeat) t;
                VoiceRoomUser member = seat.getUser();
                if (member != null) {
                    ivAccount.setVisibility(View.VISIBLE);
                    ImageLoader.with(getContext()).commonLoad(member.getAvatar(), ivAccount);
                    tvContent.setText(member.getNick() + "\t申请麦位(" + seat.getIndex() + 1 + ")");
                    holder.setVisible(R.id.iv_refuse, true);
                    holder.setVisible(R.id.iv_agree, true);
                }
            }
        }
    }
}
