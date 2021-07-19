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
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.nertc.model.NERtcVoiceRoom;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.model.interfaces.Anchor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoomMuteDialog extends BaseRecyclerDialogFragment<VoiceRoomUser> {

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

    public RoomMuteDialog(FragmentActivity activity, VoiceRoomInfo voiceRoomInfo) {
        this.mActivity = activity;
        this.mVoiceRoomInfo = voiceRoomInfo;
        this.mAnchor = NERtcVoiceRoom.sharedInstance(activity).getAnchor();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addTitle();

        fetchRoomMute();
        fetchRoomMutedMembers();
    }

    @Override
    protected void setAttributes(@NonNull WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.height = SizeUtils.dp2px(300);
        params.gravity = Gravity.BOTTOM;
    }

    private void addTitle() {
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
        mAnchor.getRoomQuery().fetchRoomMute(new RequestCallback<Boolean>() {
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

            @Override
            public void onFailed(int code) {
                ToastUtils.showShort("禁言失败code" + code);
            }

            @Override
            public void onException(Throwable exception) {
                ToastUtils.showShort("禁言失败exception" + exception.getMessage());
            }
        });
    }

    /**
     * 获取房间禁言成员列表
     */
    private void fetchRoomMutedMembers() {
        mAnchor.getRoomQuery().fetchMembersByMuted(true, new RequestCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {
                mAdapter.setNewInstance(members);

//                muteList.clear();
//                muteList.addAll(members);
//                if (muteList.isEmpty()) {
//                    return;
//                }
//                tvTitle.setText("禁言成员 (" + muteList.size() + ")");
//
//                adapter.updateDataSource(muteList);
//                adapter.setRemoveMute((p) -> {
//                    if (isAllMute) {
//                        ToastUtils.showShort("全员禁言中,不能解禁");
//                    } else {
//                        removeMuteMember(p, muteList.get(p));
//                    }
//                });
            }

            @Override
            public void onFailed(int i) {
                ToastUtils.showShort("获取禁言用户失败code" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastUtils.showShort("获取禁言用户失败Exception" + throwable.getMessage());
            }
        });
    }

    /**
     * 添加禁言成员
     */
    private void showRoomMember() {
        RoomMemberListDialog dialog = new RoomMemberListDialog();
        dialog.show(mActivity.getSupportFragmentManager(), dialog.getTag());
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
    }
}
