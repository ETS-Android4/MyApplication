package com.netease.audioroom.demo.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.widget.HeadImageView;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Reason;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Status;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

import org.jetbrains.annotations.NotNull;

/**
 * 麦位列表
 */
public class RoomSeatAdapter extends BaseQuickAdapter<VoiceRoomSeat, BaseViewHolder> {

    public RoomSeatAdapter() {
        super(R.layout.item_room_seat);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, VoiceRoomSeat seat) {
        VoiceRoomUser user = seat.getUser();
        HeadImageView ivAvatar = holder.getView(R.id.iv_user_avatar);
        ImageView ivStatusHint = holder.getView(R.id.iv_user_status_hint);
        TextView tvNick = holder.getView(R.id.tv_user_nick);
        ImageView ivStatus = holder.getView(R.id.iv_user_stats);
        ImageView circle = holder.getView(R.id.iv_circle);
        View avatarBg = holder.getView(R.id.avatar_bg);
        LottieAnimationView applying = holder.getView(R.id.lav_apply);
        switch (seat.getStatus()) {
            case Status.INIT:
                ivStatusHint.setVisibility(View.GONE);
                ivStatus.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.drawable.seat_add_member);
                circle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.APPLY:
                ivStatus.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(0);
                applying.setVisibility(View.VISIBLE);
                applying.setRepeatCount(LottieDrawable.INFINITE);
                applying.playAnimation();
                if (seat.getReason() != Reason.APPLY_MUTED) {
                    ivStatusHint.setVisibility(View.GONE);
                } else {
                    ivStatusHint.setVisibility(View.VISIBLE);
                    ivStatusHint.setImageResource(R.drawable.audio_be_muted_status);
                }
                tvNick.setText(user != null ? user.getAccount() : "");
                circle.setVisibility(View.INVISIBLE);
                break;

            case Status.ON:
                ivStatus.setVisibility(View.GONE);
                ivStatusHint.setVisibility(View.VISIBLE);
                ivStatusHint.setImageResource(R.drawable.icon_mic);
                circle.setVisibility(View.VISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.CLOSED:
                ivStatus.setVisibility(View.VISIBLE);
                ivStatusHint.setVisibility(View.GONE);
                ivStatus.setImageResource(R.drawable.close);
                circle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.FORBID:
                ivStatus.setVisibility(View.VISIBLE);
                ivStatusHint.setVisibility(View.GONE);
                ivStatus.setImageResource(R.drawable.seat_close);
                circle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.AUDIO_MUTED:
            case Status.AUDIO_CLOSED_AND_MUTED:
                ivStatus.setVisibility(View.GONE);
                ivStatusHint.setVisibility(View.VISIBLE);
                ivStatusHint.setImageResource(R.drawable.audio_be_muted_status);
                circle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.AUDIO_CLOSED:
                ivStatus.setVisibility(View.GONE);
                ivStatusHint.setVisibility(View.VISIBLE);
                ivStatusHint.setImageResource(R.drawable.icon_seat_close_micro);
                circle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
        }

        if (user != null && seat.getStatus() == Status.APPLY) {//请求麦位
            tvNick.setText(user.getNick());
            ivAvatar.loadAvatar(user.getAvatar());
            ivAvatar.setVisibility(View.VISIBLE);
            avatarBg.setVisibility(View.VISIBLE);
        } else if (seat.isOn()) {//麦上有人
            ivAvatar.loadAvatar(user.getAvatar());
            ivAvatar.setVisibility(View.VISIBLE);
            avatarBg.setVisibility(View.VISIBLE);
            tvNick.setVisibility(View.VISIBLE);
            tvNick.setText(user.getNick());
        } else {
            tvNick.setText("麦位" + (seat.getIndex() + 1));
            circle.setVisibility(View.INVISIBLE);
            ivAvatar.setVisibility(View.INVISIBLE);
            avatarBg.setVisibility(View.INVISIBLE);
        }
    }
}
