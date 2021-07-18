package com.netease.audioroom.demo.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.yunxin.android.lib.picture.ImageLoader;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

public class BaseRecycleAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseRecycleAdapter() {
        super(R.layout.item_menu);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, T t) {
        if (t instanceof String) {
            holder.setText(R.id.tv_text, t.toString());
        } else if (t instanceof VoiceRoomUser) {
            VoiceRoomUser member = (VoiceRoomUser) t;
            holder.setVisible(R.id.iv_head, true);
            ImageLoader.with(getContext()).commonLoad(member.getAvatar(), holder.findView(R.id.iv_head));
            holder.setText(R.id.tv_text, member.getNick());
        }
    }
}
