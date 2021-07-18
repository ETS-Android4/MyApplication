package com.netease.audioroom.demo.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.nertc.model.NERtcVoiceRoom;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.model.interfaces.Anchor;

import java.util.ArrayList;
import java.util.List;

public class MemberListDialog extends BaseRecyclerDialogFragment<VoiceRoomUser> {

    private final Anchor mAnchor;
    private final VoiceRoomSeat mSeat;
    private final List<String> excludeAccounts = new ArrayList<>();

    @Override
    protected BaseQuickAdapter<VoiceRoomUser, BaseViewHolder> setAdapter() {
        return new BaseRecycleAdapter<>();
    }

    public MemberListDialog(FragmentActivity activity, VoiceRoomSeat seat, List<VoiceRoomSeat> seats) {
        this.mSeat = seat;
        List<String> accounts = new ArrayList<>();
        for (VoiceRoomSeat s : seats) {
            if (s.isOn()) {
                String account = s.getAccount();
                if (!TextUtils.isEmpty(account)) {
                    accounts.add(account);
                }
            }
        }
        if (!accounts.isEmpty()) {
            this.excludeAccounts.addAll(accounts);
        }
        this.mAnchor = NERtcVoiceRoom.sharedInstance(activity).getAnchor();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchRoomMembers(mAnchor, excludeAccounts);
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

    private void fetchRoomMembers(Anchor anchor, List<String> excludeAccounts) {
        RequestCallback<List<VoiceRoomUser>> callback = new RequestCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {
                if (CollectionUtils.isNotEmpty(members)) {
                    mAdapter.setNewInstance(members);
                } else {
                    onEmptyView();
                }
            }

            @Override
            public void onFailed(int i) {
                ToastUtils.showShort("获取用户失败code" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastUtils.showShort("获取用户失败Exception" + throwable.getMessage());
            }
        };
        if (!excludeAccounts.isEmpty()) {
            anchor.getRoomQuery().fetchMembersByAccount(excludeAccounts, false, callback);
        } else {
            anchor.getRoomQuery().fetchMembersByMuted(false, callback);
        }
    }
}