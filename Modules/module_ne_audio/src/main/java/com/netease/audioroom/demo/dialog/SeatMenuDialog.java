package com.netease.audioroom.demo.dialog;

import android.util.Log;

import com.example.william.my.library.base.BaseDialogFragment;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.widget.OnItemClickListener;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;

import java.util.ArrayList;
import java.util.List;

public class SeatMenuDialog extends BaseDialogFragment {

    private final VoiceRoomSeat mSeat;
    private final List<String> mItems;

    public SeatMenuDialog(VoiceRoomSeat seat) {
        this.mSeat = seat;
        this.mItems = new ArrayList<>();
    }


    @Override
    protected int getLayout() {
        return super.getLayout();
    }

    private void addItem() {
        Log.e("TAG", "add");
        switch (mSeat.getStatus()) {
            // 抱观众上麦（点击麦位）
            case VoiceRoomSeat.Status.INIT:
                mItems.add("将成员抱上麦位");
                mItems.add("屏蔽麦位");
                mItems.add("关闭麦位");
                break;
            // 当前存在有效用户
            case VoiceRoomSeat.Status.ON:
                // 当前麦位已经关闭
            case VoiceRoomSeat.Status.AUDIO_CLOSED:
                mItems.add("将TA踢下麦位");
                mItems.add("屏蔽麦位");
                break;
            // 当前麦位已经被关闭
            case VoiceRoomSeat.Status.CLOSED:
                mItems.add("打开麦位");
                break;
            // 且当前麦位无人，麦位禁麦触发
            case VoiceRoomSeat.Status.FORBID:
                mItems.add("将成员抱上麦位");
                mItems.add("解除语音屏蔽");
                break;
            // 当前麦位已经禁麦或已经关闭
            case VoiceRoomSeat.Status.AUDIO_MUTED:
            case VoiceRoomSeat.Status.AUDIO_CLOSED_AND_MUTED:
                mItems.add("将TA踢下麦位");
                mItems.add("解除语音屏蔽");
                break;
        }
        mItems.add(getString(R.string.cancel));
    }

    private OnItemClickListener<String> onItemClickListener;

    public SeatMenuDialog setOnItemClickListener(OnItemClickListener<String> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }
}
