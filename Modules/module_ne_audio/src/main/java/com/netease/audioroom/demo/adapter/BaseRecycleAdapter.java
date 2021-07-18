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
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

public class BaseRecycleAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseRecycleAdapter() {
        super(R.layout.item_base_recycle);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, T t) {
        ImageView imageView = holder.findView(R.id.iv_head);
        TextView textView = holder.findView(R.id.tv_text);
        if (textView != null && imageView != null) {
            if (t instanceof String) {
                textView.setGravity(Gravity.CENTER);
                textView.setText(t.toString());
            } else if (t instanceof VoiceRoomUser) {
                VoiceRoomUser member = (VoiceRoomUser) t;
                imageView.setVisibility(View.VISIBLE);
                ImageLoader.with(getContext()).commonLoad(member.getAvatar(), imageView);
                holder.setText(R.id.tv_text, member.getNick());
            }
        }
    }
}
