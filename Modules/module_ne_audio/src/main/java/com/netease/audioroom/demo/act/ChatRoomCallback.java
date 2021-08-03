package com.netease.audioroom.demo.act;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.audioroom.demo.adapter.RoomMessageAdapter;
import com.netease.audioroom.demo.adapter.RoomSeatAdapter;
import com.netease.audioroom.demo.databinding.ActivityAnchorBinding;
import com.netease.audioroom.demo.dialog.RoomMuteListDialog;
import com.netease.audioroom.demo.util.InputUtils;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomInfo;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomMessage;
import com.netease.yunxin.voiceroom.model.bean.VoiceRoomSeat;

import java.util.List;

public class ChatRoomCallback implements IChatRoomCallback {

    private final FragmentActivity mActivity;
    private final ActivityAnchorBinding mViewBinding;

    private final boolean isAnchorMode;
    private final VoiceRoomInfo mVoiceRoomInfo;

    protected RoomSeatAdapter mRoomSeatAdapter;
    protected GridLayoutManager mSeatLayoutManager;

    protected RoomMessageAdapter mMsgAdapter;
    protected LinearLayoutManager mMsgLayoutManager;

    public ChatRoomCallback(FragmentActivity activity, ActivityAnchorBinding binding, boolean anchor, VoiceRoomInfo roomInfo) {
        this.mActivity = activity;
        this.mViewBinding = binding;
        this.isAnchorMode = anchor;
        this.mVoiceRoomInfo = roomInfo;
        initView();
    }

    private void initView() {
        mViewBinding.include.ivLeaveRoom.setOnClickListener(v -> {
                    if (isAnchorMode) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("确认结束直播？")
                                .setMessage("请确认是否结束直播")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ChatRoomManager.getInstance().toggleLeaveRoom();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create()
                                .show();
                    } else {
                        mActivity.finish();
                    }
                }
        );

        String name = mVoiceRoomInfo.getName();
        name = TextUtils.isEmpty(name) ? mVoiceRoomInfo.getRoomId() : name;
        mViewBinding.include.tvRoomName.setText(name);

        String count = "在线" + mVoiceRoomInfo.getOnlineUserCount() + "人";
        mViewBinding.include.tvRoomMemberCount.setText(count);

        mViewBinding.include.tvRoomAnnouncement.setOnClickListener(v -> {

                }
        );
        mViewBinding.include.ivRoomAudio.setOnClickListener(v -> {

                }
        );
        mViewBinding.include.ivRoomMic.setOnClickListener(view -> {

                }
        );

        if (isAnchorMode) {
            mViewBinding.include.ivRoomMute.setVisibility(View.VISIBLE);
            mViewBinding.include.ivRoomMore.setVisibility(View.VISIBLE);
        }
        mViewBinding.include.ivRoomMute.setOnClickListener(view -> {
            RoomMuteListDialog dialog = new RoomMuteListDialog(mActivity, mVoiceRoomInfo);
            dialog.show(mActivity.getSupportFragmentManager(), dialog.getTag());
        });
        mViewBinding.include.ivRoomMore.setOnClickListener(view -> {

        });

        mViewBinding.include.etInputText.setOnEditorActionListener((v, actionId, event) -> {
            InputUtils.hideSoftInput(mActivity, mViewBinding.include.etInputText);
            String content = mViewBinding.include.etInputText.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showShort("请输入消息内容");
            }

            return true;
        });

        mSeatLayoutManager = new GridLayoutManager(mActivity, 4);
        mSeatLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 4 : 1;
            }
        });
        mViewBinding.include.recyclerviewSeat.setLayoutManager(mSeatLayoutManager);

        mRoomSeatAdapter = new RoomSeatAdapter();
        mViewBinding.include.recyclerviewSeat.setAdapter(mRoomSeatAdapter);
        mRoomSeatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });

        mMsgLayoutManager = new LinearLayoutManager(mActivity);
        mViewBinding.include.recyclerviewMessage.setLayoutManager(mMsgLayoutManager);
        mMsgAdapter = new RoomMessageAdapter();
        mViewBinding.include.recyclerviewMessage.setAdapter(mMsgAdapter);
    }

    @Override
    public void onEnterRoom(boolean success) {

    }

    @Override
    public void onLeaveRoom() {

    }

    @Override
    public void onRoomDismiss() {

    }

    @Override
    public void onOnlineUserCount(int onlineUserCount) {

    }

    @Override
    public void onUpdateSeats(List<VoiceRoomSeat> seats) {
        mRoomSeatAdapter.setNewInstance(seats);
    }

    @Override
    public void onUpdateSeat(VoiceRoomSeat seat) {
        mRoomSeatAdapter.notifyItemChanged(seat.getIndex(), seat);
    }

    @Override
    public void onApplySeats(List<VoiceRoomSeat> seats) {

    }

    @Override
    public void updateVolume(int index, int volume) {

    }

    @Override
    public void onSeatMuted() {

    }

    @Override
    public void onSeatClosed() {

    }

    @Override
    public void onTextMuted(boolean muted) {

    }

    @Override
    public void onVoiceRoomMessage(VoiceRoomMessage message) {

    }
}