package com.netease.audioroom.demo.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomInfo;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomUser;
import com.netease.yunxin.voiceroom.model.callback.SuccessCallback;
import com.netease.yunxin.voiceroom.model.interfaces.Anchor;
import com.netease.yunxin.voiceroom.model.interfaces.NERtcVoiceRoom;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 禁言列表
 */
public class RoomMuteListDialog extends BaseRecyclerDialogFragment<VoiceRoomUser> {

    private final FragmentActivity mActivity;

    private final Anchor mAnchor;
    private final VoiceRoomInfo mVoiceRoomInfo;

    public boolean isAllMute;

    private TextView tvMuteAdd;
    private TextView tvMuteAll;

    @Override
    protected BaseQuickAdapter<VoiceRoomUser, BaseViewHolder> setAdapter() {
        return new BaseRecycleAdapter<>();
    }

    @Override
    protected boolean canRefresh() {
        return false;
    }

    public RoomMuteListDialog(FragmentActivity activity, VoiceRoomInfo voiceRoomInfo) {
        this.mActivity = activity;
        this.mVoiceRoomInfo = voiceRoomInfo;
        this.mAnchor = NERtcVoiceRoom.sharedInstance(activity).getAnchor();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addHeaderView();

        fetchRoomMute();

        fetchRoomMutedMembers();
    }

    @Override
    public void onItemClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
        super.onItemClick(adapter, view, position);
        VoiceRoomUser member = (VoiceRoomUser) adapter.getData().get(position);
        ChatRoomHelper.muteMember(member, false, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastUtils.showShort(member.getNick() + "已被解除禁言");
            }
        });
        dismiss();
    }

    @Override
    protected void setAttributes(@NonNull WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.height = SizeUtils.dp2px(300);
        params.gravity = Gravity.BOTTOM;
    }

    private void addHeaderView() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        tvMuteAdd = new TextView(getContext());
        tvMuteAdd.setGravity(Gravity.CENTER);
        tvMuteAdd.setText("添加禁言成员");
        tvMuteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoomMember();
            }
        });

        tvMuteAll = new TextView(getContext());
        tvMuteAll.setGravity(Gravity.CENTER);
        tvMuteAll.setText("全部禁言");
        tvMuteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteAllMember(!isAllMute);
            }
        });

        linearLayout.addView(tvMuteAdd, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48), 1f));
        linearLayout.addView(tvMuteAll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48), 1f));

        mAdapter.addHeaderView(linearLayout);
    }

    /**
     * 房间禁言状态
     */
    private void fetchRoomMute() {
        ChatRoomHelper.fetchRoomMute(new SuccessCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isMute) {
                if (isMute) {
                    isAllMute = true;
                    tvMuteAll.setText("取消全部禁言");
                } else {
                    isAllMute = false;
                    tvMuteAll.setText("全部禁言");
                }
            }
        });
    }

    /**
     * 获取房间禁言成员列表
     */
    private void fetchRoomMutedMembers() {
        mAnchor.getRoomQuery().fetchMembersByMuted(true, new SuccessCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {
                mAdapter.setNewInstance(members);
            }
        });
    }

    /**
     * 添加禁言成员
     */
    private void showRoomMember() {
        RoomMemberListDialog dialog = new RoomMemberListDialog();
        dialog.show(mActivity.getSupportFragmentManager(), dialog.getTag());
        dismiss();
    }

    /**
     * 全部禁言
     */
    private void muteAllMember(boolean mute) {
        ChatRoomHttpClient.getInstance().muteAll(
                DemoCache.getAccountId(), mVoiceRoomInfo.getRoomId(), mute, true, false,
                new ChatRoomHttpClient.ChatRoomHttpCallback<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        if (!isAllMute) {
                            tvMuteAll.setText("取消全部禁麦");
                            ToastUtils.showShort("已全部禁麦");
                        } else {
                            tvMuteAll.setText("全部禁言");
                            ToastUtils.showShort("取消全部禁麦");
                        }
                        isAllMute = mute;
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        ToastUtils.showShort("全部禁麦失败+" + errorMsg);
                    }
                });
        dismiss();
    }
}
