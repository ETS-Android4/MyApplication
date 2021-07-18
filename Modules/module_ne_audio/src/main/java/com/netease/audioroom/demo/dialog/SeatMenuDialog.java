package com.netease.audioroom.demo.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.util.SuccessCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 麦位点击弹窗
 */
public class SeatMenuDialog extends BaseRecyclerDialogFragment<String> {

    private final FragmentActivity mActivity;

    private final VoiceRoomSeat mSeat;
    private final List<String> mMenus = new ArrayList<>();

    @Override
    protected BaseQuickAdapter<String, BaseViewHolder> setAdapter() {
        return new BaseRecycleAdapter<>();
    }

    @Override
    protected boolean canRefresh() {
        return false;
    }

    public SeatMenuDialog(FragmentActivity activity, VoiceRoomSeat seat) {
        this.mActivity = activity;
        this.mSeat = seat;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        addMenuItem(mSeat);
        mAdapter.setNewInstance(mMenus);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        super.onItemClick(adapter, view, position);
        onSeatAction(mSeat, adapter.getData().get(position).toString());
        dismiss();
    }

    @Override
    protected void setAttributes(@NonNull WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.height = SizeUtils.dp2px(300);
        params.gravity = Gravity.BOTTOM;
    }

    private void addMenuItem(VoiceRoomSeat seat) {
        switch (seat.getStatus()) {
            // 抱观众上麦（点击麦位）
            case VoiceRoomSeat.Status.INIT:
                mMenus.add("将成员抱上麦位");
                mMenus.add("屏蔽麦位");
                mMenus.add("关闭麦位");
                break;
            // 当前存在有效用户
            case VoiceRoomSeat.Status.ON:
                // 当前麦位已经关闭
            case VoiceRoomSeat.Status.AUDIO_CLOSED:
                mMenus.add("将TA踢下麦位");
                mMenus.add("屏蔽麦位");
                break;
            // 当前麦位已经被关闭
            case VoiceRoomSeat.Status.CLOSED:
                mMenus.add("打开麦位");
                break;
            // 且当前麦位无人，麦位禁麦触发
            case VoiceRoomSeat.Status.FORBID:
                mMenus.add("将成员抱上麦位");
                mMenus.add("解除语音屏蔽");
                break;
            // 当前麦位已经禁麦或已经关闭
            case VoiceRoomSeat.Status.AUDIO_MUTED:
            case VoiceRoomSeat.Status.AUDIO_CLOSED_AND_MUTED:
                mMenus.add("将TA踢下麦位");
                mMenus.add("解除语音屏蔽");
                break;
        }
        mMenus.add(requireActivity().getString(R.string.cancel));
    }

    private void onSeatAction(VoiceRoomSeat seat, @NonNull String item) {
        switch (item) {
            case "将成员抱上麦位":
                //获取成员列表
                ChatRoomHelper.fetchMemberList(new SuccessCallback<List<VoiceRoomSeat>>() {
                    @Override
                    public void onSuccess(List<VoiceRoomSeat> seats) {
                        MemberListDialog dialog = new MemberListDialog(mActivity, seat, seats);
                        dialog.show(requireActivity().getSupportFragmentManager(), dialog.getTag());
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
