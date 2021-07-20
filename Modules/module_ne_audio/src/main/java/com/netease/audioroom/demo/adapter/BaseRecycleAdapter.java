package com.netease.audioroom.demo.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.yunxin.android.lib.picture.ImageLoader;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

public class BaseRecycleAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseRecycleAdapter() {
        super(R.layout.item_base_recycle);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, T t) {
        ImageView ivAccount = holder.findView(R.id.iv_account);
        TextView tvContent = holder.findView(R.id.tv_content);
        ImageView ivRefuse = holder.findView(R.id.iv_refuse);
        ImageView ivAgree = holder.findView(R.id.iv_agree);
        if (tvContent != null && ivAccount != null &&
                ivRefuse != null && ivAgree != null) {
            if (t instanceof String) {
                //菜单
                Log.e("TAG", "String");
                tvContent.setGravity(Gravity.CENTER);
                tvContent.setText(t.toString());
            } else if (t instanceof VoiceRoomUser) {
                //成员列表，禁言列表
                Log.e("TAG", "VoiceRoomUser");
                VoiceRoomUser member = (VoiceRoomUser) t;
                ivAccount.setVisibility(View.VISIBLE);
                ImageLoader.with(getContext()).commonLoad(member.getAvatar(), ivAccount);
                holder.setText(R.id.tv_content, member.getNick());
            } else if (t instanceof VoiceRoomSeat) {
                Log.e("TAG", "VoiceRoomSeat");
                VoiceRoomSeat seat = (VoiceRoomSeat) t;
                VoiceRoomUser member = seat.getUser();
                if (member != null) {
                    ivAccount.setVisibility(View.VISIBLE);
                    ImageLoader.with(getContext()).commonLoad(member.getAvatar(), ivAccount);
                    tvContent.setText(member.getNick() + "\t申请麦位(" + seat.getIndex() + 1 + ")");
                    ivRefuse.setVisibility(View.VISIBLE);
                    ivAgree.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
