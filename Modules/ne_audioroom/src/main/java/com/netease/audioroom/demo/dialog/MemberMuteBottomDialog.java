package com.netease.audioroom.demo.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.MuteMemberListAdapter;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.nertc.model.NERtcVoiceRoom;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.model.interfaces.Anchor;

import java.util.ArrayList;
import java.util.List;

/**
 * 禁言列表
 */
public class MemberMuteBottomDialog extends BaseBottomDialog {

    private final List<VoiceRoomUser> muteList = new ArrayList<>();
    private final VoiceRoomInfo voiceRoomInfo;
    private final Anchor anchor;

    private TextView tvMuteAll;
    private TextView tvTitle;
    private MuteMemberListAdapter adapter;
    public boolean isAllMute;

    public MemberMuteBottomDialog(@NonNull Activity activity, VoiceRoomInfo voiceRoomInfo) {
        super(activity);
        this.voiceRoomInfo = voiceRoomInfo;
        this.anchor = NERtcVoiceRoom.sharedInstance(activity).getAnchor();
    }

    @Override
    protected void renderTopView(FrameLayout parent) {
        tvTitle = new TextView(getContext());
        tvTitle.setText("禁言成员");
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(Color.parseColor("#ff333333"));
        FrameLayout.LayoutParams titleLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parent.addView(tvTitle, titleLayoutParams);

        TextView cancelView = new TextView(getContext());
        cancelView.setText("取消");
        cancelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        cancelView.setPadding(SizeUtils.dp2px(20), 0, 0, 0);
        cancelView.setGravity(Gravity.CENTER);
        cancelView.setTextColor(Color.parseColor("#ff333333"));
        FrameLayout.LayoutParams cancelLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parent.addView(cancelView, cancelLayoutParams);

        cancelView.setOnClickListener(v -> dismiss());
    }

    @Override
    protected void renderBottomView(FrameLayout parent) {
        View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_more_muter_member, parent);
        View tvAddMuteMember = bottomView.findViewById(R.id.tv_add_mute_member);
        tvAddMuteMember.setOnClickListener(v -> addMuteMember());

        tvMuteAll = bottomView.findViewById(R.id.tv_mute_all_members);
        tvMuteAll.setOnClickListener(v -> muteAllMember(!isAllMute));

        RecyclerView rvMemberList = bottomView.findViewById(R.id.rv_member_list);
        rvMemberList.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight() * 0.8) - SizeUtils.dp2px((108));
        rvMemberList.requestLayout();
        rvMemberList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MuteMemberListAdapter(getContext());
        rvMemberList.setAdapter(adapter);

        fetchRoomMute();
        fetchMutedRoomMembers();

    }

    private void fetchRoomMute() {
        anchor.getRoomQuery().fetchRoomMute(new RequestCallback<Boolean>() {
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

    private void fetchMutedRoomMembers() {
        anchor.getRoomQuery().fetchMembersByMuted(true, new RequestCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {

                muteList.clear();
                muteList.addAll(members);
                if (muteList.isEmpty()) {
                    return;
                }
                tvTitle.setText("禁言成员 (" + muteList.size() + ")");

                adapter.updateDataSource(muteList);
                adapter.setRemoveMute((p) -> {
                    if (isAllMute) {
                        ToastUtils.showShort("全员禁言中,不能解禁");
                    } else {
                        removeMuteMember(p, muteList.get(p));
                    }
                });

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

    private void addMuteMember() {
        new MemberSelectBottomDialog(activity, member -> {
            if (member == null) {
                return;
            }

            muteList.add(0, member);

            anchor.getRoomQuery().muteMember(member, true, new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    String nick = member.getNick();
                    ToastUtils.showShort(nick + "已被禁言");

                    ArrayList<String> accountList = new ArrayList<>();
                    for (String account : accountList) {
                        accountList.add(0, account);
                    }
                    adapter.updateDataSource(muteList);
                    tvTitle.setText("禁言成员 (" + muteList.size() + ")");
                    adapter.setRemoveMute((p) -> {
                        if (isAllMute) {
                            ToastUtils.showShort("全员禁言中,不能解禁");
                        } else {
                            removeMuteMember(p, muteList.get(p));
                        }
                    });
                }

                @Override
                public void onFailed(int code) {
                    // 失败
                    ToastUtils.showShort("禁言失败" + code);
                }

                @Override
                public void onException(Throwable exception) {
                    // 错误
                    ToastUtils.showShort("禁言异常" + exception.getMessage());
                }
            });

        }).show();
    }

    private void muteAllMember(boolean mute) {
        ChatRoomHttpClient.getInstance().muteAll(DemoCache.getAccountId(), voiceRoomInfo.getRoomId(), mute, true, false,
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

    private void removeMuteMember(int p, VoiceRoomUser member) {
        anchor.getRoomQuery().muteMember(member, false, new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastUtils.showShort(member.getNick() + "已被解除禁言");
                muteList.remove(p);
                adapter.removeItem(p);
                if (muteList.isEmpty()) {
                    tvTitle.setText("禁言成员");
                } else {
                    tvTitle.setText("禁言成员 (" + muteList.size() + ")");
                }
            }

            @Override
            public void onFailed(int code) {
                ToastUtils.showShort("解禁失败" + code);
            }

            @Override
            public void onException(Throwable exception) {
                ToastUtils.showShort("解禁异常" + exception.getMessage());
            }
        });
    }
}
