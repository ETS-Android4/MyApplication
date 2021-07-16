package com.netease.audioroom.demo.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.util.SuccessCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 麦位点击弹窗
 */
public class SeatMenuBottomDialog extends BaseBottomDialog {

    private Activity activity;
    private final List<String> items = new ArrayList<>();

    private VoiceRoomSeat mSeat;

    public SeatMenuBottomDialog(@NonNull Activity activity, VoiceRoomSeat seat) {
        super(activity);
        this.activity = activity;
        this.mSeat = seat;
        addMenuItem();
    }

    @Override
    protected void renderTopView(FrameLayout parent) {
    }

    @Override
    protected boolean enableTop() {
        return false;
    }

    @Override
    protected void renderBottomView(FrameLayout parent) {
        RecyclerView rvMemberList = new RecyclerView(getContext());
        rvMemberList.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        rvMemberList.setLayoutManager(new LinearLayoutManager(getContext()));

        BaseRecycleAdapter<String> mAdapter = new BaseRecycleAdapter<>();
        rvMemberList.setAdapter(mAdapter);

        mAdapter.setNewInstance(items);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                onSeatAction(mSeat, adapter.getData().get(position).toString());
                dismiss();
            }
        });
        parent.addView(rvMemberList, layoutParams);
    }

    private void addMenuItem() {
        switch (mSeat.getStatus()) {
            // 抱观众上麦（点击麦位）
            case VoiceRoomSeat.Status.INIT:
                items.add("将成员抱上麦位");
                items.add("屏蔽麦位");
                items.add("关闭麦位");
                break;
            // 当前存在有效用户
            case VoiceRoomSeat.Status.ON:
                // 当前麦位已经关闭
            case VoiceRoomSeat.Status.AUDIO_CLOSED:
                items.add("将TA踢下麦位");
                items.add("屏蔽麦位");
                break;
            // 当前麦位已经被关闭
            case VoiceRoomSeat.Status.CLOSED:
                items.add("打开麦位");
                break;
            // 且当前麦位无人，麦位禁麦触发
            case VoiceRoomSeat.Status.FORBID:
                items.add("将成员抱上麦位");
                items.add("解除语音屏蔽");
                break;
            // 当前麦位已经禁麦或已经关闭
            case VoiceRoomSeat.Status.AUDIO_MUTED:
            case VoiceRoomSeat.Status.AUDIO_CLOSED_AND_MUTED:
                items.add("将TA踢下麦位");
                items.add("解除语音屏蔽");
                break;
        }
        items.add(activity.getString(R.string.cancel));
    }

    private void onSeatAction(VoiceRoomSeat seat, String item) {
        switch (item) {
            case "将成员抱上麦位":
                //获取成员列表
                ChatRoomHelper.fetchMemberList(new SuccessCallback<List<VoiceRoomSeat>>() {
                    @Override
                    public void onSuccess(List<VoiceRoomSeat> seats) {
                        //展示成员列表
                        new MemberSelectBottomDialog(null, seats, new MemberSelectBottomDialog.OnMemberChosenListener() {
                            @Override
                            public void onMemberChosen(VoiceRoomUser member) {
                                //被抱用户
                                if (member != null) {
                                    ChatRoomHelper.checkIsRoomMember(seat.getIndex(), member);
                                }
                            }
                        }).show();
                    }
                });
                break;
            case "将TA踢下麦位":
                ChatRoomHelper.kickSeat(seat);
                break;
            case "关闭麦位":
                ChatRoomHelper.closeSeat(seat);
                break;
            case "屏蔽麦位":
                ChatRoomHelper.muteSeat(seat);
                break;
            case "解除语音屏蔽":
            case "打开麦位":
                ChatRoomHelper.openSeat(seat);
                break;
            case "退出房间":
                ChatRoomHelper.leaveRoom();
                break;
        }
    }
}
