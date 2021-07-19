package com.netease.audioroom.demo.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.google.gson.Gson;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.util.SuccessCallback;

import java.util.ArrayList;
import java.util.List;

public class RoomMemberListDialog extends BaseRecyclerDialogFragment<VoiceRoomUser> {

    private VoiceRoomSeat mSeat;
    private final List<String> excludeAccounts = new ArrayList<>();

    @Override
    protected BaseQuickAdapter<VoiceRoomUser, BaseViewHolder> setAdapter() {
        return new BaseRecycleAdapter<>();
    }

    @Override
    protected boolean canRefresh() {
        return false;
    }

    public RoomMemberListDialog() {

    }

    public RoomMemberListDialog(VoiceRoomSeat seat) {
        this.mSeat = seat;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchRoomSeats();
    }

    @Override
    protected void setAttributes(@NonNull WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.height = SizeUtils.dp2px(300);
        params.gravity = Gravity.BOTTOM;
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        super.onItemClick(adapter, view, position);
        VoiceRoomUser member = (VoiceRoomUser) adapter.getData().get(position);
        if (member != null) {
            ChatRoomHelper.checkIsRoomMember(mSeat.getIndex(), member);
        }
        dismiss();
    }

    /**
     * 获取房间内麦位列表
     */
    private void fetchRoomSeats() {
        ChatRoomHelper.fetchRoomSeats(new SuccessCallback<List<VoiceRoomSeat>>() {
            @Override
            public void onSuccess(List<VoiceRoomSeat> seats) {
                for (VoiceRoomSeat s : seats) {
                    Log.e("TAG", new Gson().toJson(s));
                    if (s.isOn()) {
                        String account = s.getAccount();
                        if (!TextUtils.isEmpty(account)) {
                            excludeAccounts.add(account);
                        }
                    }
                }
                fetchRoomMembers(excludeAccounts);
            }
        });
    }

    /**
     * 获取房间内成员列表
     */
    private void fetchRoomMembers(List<String> excludeAccounts) {
        ChatRoomHelper.fetchRoomMembers(excludeAccounts, new SuccessCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {
                for (VoiceRoomUser m : members) {
                    Log.e("TAG", new Gson().toJson(m));
                }
                if (CollectionUtils.isNotEmpty(members)) {
                    mAdapter.setNewInstance(members);
                } else {
                    onEmptyView();
                }
            }
        });
    }
}