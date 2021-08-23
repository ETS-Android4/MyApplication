package com.netease.audioroom.demo.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;

import org.jetbrains.annotations.NotNull;

/**
 * 麦位申请列表
 */
public class RoomSeatApplyListDialog extends BaseRecyclerDialogFragment<VoiceRoomSeat> {

    @Override
    protected BaseQuickAdapter<VoiceRoomSeat, BaseViewHolder> setAdapter() {
        BaseRecycleAdapter<VoiceRoomSeat> adapter = new BaseRecycleAdapter<>();
        adapter.addChildClickViewIds(R.id.iv_refuse, R.id.iv_agree);
        return adapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setNewInstance(ChatRoomHelper.getApplySeats());
    }

    @Override
    protected void setAttributes(@NonNull WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.height = SizeUtils.dp2px(300);
        params.gravity = Gravity.BOTTOM;
    }

    @Override
    public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
        super.onItemChildClick(adapter, view, position);
        VoiceRoomSeat seat = (VoiceRoomSeat) adapter.getData().get(position);
        if (view.getId() == R.id.iv_refuse) {
            ChatRoomHelper.denySeatApply(seat);
        } else if (view.getId() == R.id.iv_agree) {
            ChatRoomHelper.agreeSeatApply(seat);
        }
        dismiss();
    }
}
