package com.netease.audioroom.demo.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat.Reason;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat.Status;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;
import com.netease.audioroom.demo.widget.HeadImageView;

/**
 * 麦位列表
 */
public class RoomSeatAdapter extends BaseQuickAdapter<VoiceRoomSeat, BaseViewHolder> {

    public RoomSeatAdapter() {
        super(R.layout.item_room_seat);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, @NonNull VoiceRoomSeat seat) {
        VoiceRoomUser user = seat.getUser();

        View frame = holder.getView(R.id.frame);

        HeadImageView ivAvatar = holder.getView(R.id.iv_user_avatar);
        ImageView ivCircle = holder.getView(R.id.iv_user_circle);
        ImageView ivAudio = holder.getView(R.id.iv_user_audio);
        TextView tvNick = holder.getView(R.id.tv_user_nick);

        ImageView ivStatus = holder.getView(R.id.iv_user_stats);
        LottieAnimationView applying = holder.getView(R.id.lottie_apply);

        switch (seat.getStatus()) {
            case Status.INIT:
                ivAudio.setVisibility(View.GONE);
                ivStatus.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.drawable.seat_add_member);
                ivCircle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.APPLY:
                ivStatus.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(0);
                applying.setVisibility(View.VISIBLE);
                applying.setRepeatCount(LottieDrawable.INFINITE);
                applying.playAnimation();
                if (seat.getReason() != Reason.APPLY_MUTED) {
                    ivAudio.setVisibility(View.GONE);
                } else {
                    ivAudio.setVisibility(View.VISIBLE);
                    ivAudio.setImageResource(R.drawable.audio_be_muted_status);
                }
                tvNick.setText(user != null ? user.getAccount() : "");
                ivCircle.setVisibility(View.INVISIBLE);
                break;

            case Status.ON:
                ivStatus.setVisibility(View.GONE);
                ivAudio.setVisibility(View.VISIBLE);
                ivAudio.setImageResource(R.drawable.icon_mic);
                ivCircle.setVisibility(View.VISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.CLOSED:
                ivStatus.setVisibility(View.VISIBLE);
                ivAudio.setVisibility(View.GONE);
                ivStatus.setImageResource(R.drawable.close);
                ivCircle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.FORBID:
                ivStatus.setVisibility(View.VISIBLE);
                ivAudio.setVisibility(View.GONE);
                ivStatus.setImageResource(R.drawable.seat_close);
                ivCircle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.AUDIO_MUTED:
            case Status.AUDIO_CLOSED_AND_MUTED:
                ivStatus.setVisibility(View.GONE);
                ivAudio.setVisibility(View.VISIBLE);
                ivAudio.setImageResource(R.drawable.audio_be_muted_status);
                ivCircle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
            case Status.AUDIO_CLOSED:
                ivStatus.setVisibility(View.GONE);
                ivAudio.setVisibility(View.VISIBLE);
                ivAudio.setImageResource(R.drawable.icon_seat_close_micro);
                ivCircle.setVisibility(View.INVISIBLE);
                applying.setVisibility(View.GONE);
                break;
        }

        if (user != null && seat.getStatus() == Status.APPLY) {//请求麦位
            tvNick.setText(user.getNick());
            ivAvatar.loadAvatar(user.getAvatar());
            ivAvatar.setVisibility(View.VISIBLE);
            //frame.setVisibility(View.VISIBLE);
        } else if (user != null && seat.isOn()) {//麦上有人
            ivAvatar.loadAvatar(user.getAvatar());
            ivAvatar.setVisibility(View.VISIBLE);
            //frame.setVisibility(View.VISIBLE);
            tvNick.setVisibility(View.VISIBLE);
            tvNick.setText(user.getNick());
        } else {
            tvNick.setText("麦位" + (seat.getIndex() + 1));
            ivCircle.setVisibility(View.INVISIBLE);
            ivAvatar.setVisibility(View.INVISIBLE);
            //frame.setVisibility(View.INVISIBLE);
        }
    }
}
