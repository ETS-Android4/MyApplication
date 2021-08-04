package com.netease.audioroom.demo.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;
import com.netease.audioroom.demo.voiceroom.callback.SuccessCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 成员列表
 */
public class RoomMemberListDialog extends BaseRecyclerDialogFragment<VoiceRoomUser> {

    private final Click_Type mType;

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
        this.mType = Click_Type.mute;
    }

    public RoomMemberListDialog(VoiceRoomSeat seat) {
        this.mSeat = seat;
        this.mType = Click_Type.invite;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mType == Click_Type.invite) {
            fetchRoomSeats();
        } else if (mType == Click_Type.mute) {
            fetchRoomMute();
        }
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
            if (mType == Click_Type.invite) {
                //邀请上麦
                ChatRoomHelper.checkIsRoomMember(mSeat.getIndex(), member);
            } else if (mType == Click_Type.mute) {
                //禁言
                ChatRoomHelper.muteMember(member, true, new SuccessCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        ToastUtils.showShort(member.getNick() + "已被禁言");
                    }
                });
            }
        }
        dismiss();
    }

    /**
     * 获取房间内麦上成员列表
     */
    private void fetchRoomSeats() {
        ChatRoomHelper.fetchRoomSeats(new SuccessCallback<List<VoiceRoomSeat>>() {
            @Override
            public void onSuccess(List<VoiceRoomSeat> seats) {
                for (VoiceRoomSeat s : seats) {
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
     *
     * @param excludeAccounts 排除麦上用户
     */
    private void fetchRoomMembers(List<String> excludeAccounts) {
        ChatRoomHelper.fetchMembersByAccount(excludeAccounts, new SuccessCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {
                if (CollectionUtils.isNotEmpty(members)) {
                    mAdapter.setNewInstance(members);
                } else {
                    onEmptyView();
                }
            }
        });
    }

    /**
     * 获取房间内 禁言列表
     */
    private void fetchRoomMute() {
        ChatRoomHelper.fetchMembersByMuted(new SuccessCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {
                if (CollectionUtils.isNotEmpty(members)) {
                    mAdapter.setNewInstance(members);
                } else {
                    onEmptyView();
                }
            }
        });
    }


    public enum Click_Type {
        invite, mute // 邀请，禁言
    }
}