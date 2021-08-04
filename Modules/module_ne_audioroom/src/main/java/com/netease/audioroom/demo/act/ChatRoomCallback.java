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
import com.netease.audioroom.demo.databinding.ActivityBaseAudioBinding;
import com.netease.audioroom.demo.dialog.RoomMuteListDialog;
import com.netease.audioroom.demo.util.InputUtils;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomMessage;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;

import java.util.List;

public class ChatRoomCallback implements IChatRoomCallback {

    private final FragmentActivity mActivity;
    private final ActivityBaseAudioBinding mViewBinding;

    private final boolean isAnchorMode;
    private final VoiceRoomInfo mVoiceRoomInfo;

    protected RoomSeatAdapter mRoomSeatAdapter;
    protected GridLayoutManager mSeatLayoutManager;

    protected RoomMessageAdapter mMsgAdapter;
    protected LinearLayoutManager mMsgLayoutManager;

    public ChatRoomCallback(FragmentActivity activity, ActivityBaseAudioBinding binding, boolean anchor, VoiceRoomInfo roomInfo) {
        this.mActivity = activity;
        this.mViewBinding = binding;
        this.isAnchorMode = anchor;
        this.mVoiceRoomInfo = roomInfo;
        initView();
    }

    private void initView() {
        mViewBinding.ivLeaveRoom.setOnClickListener(v -> {
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
        mViewBinding.tvRoomName.setText(name);

        String count = "在线" + mVoiceRoomInfo.getOnlineUserCount() + "人";
        mViewBinding.tvRoomMemberCount.setText(count);

        mViewBinding.tvRoomAnnouncement.setOnClickListener(v ->
                ToastUtils.showShort("房间公告")
        );

        mViewBinding.ivRoomMic.setOnClickListener(view ->
                ChatRoomManager.getInstance().toggleCloseLocalMic()
        );
        mViewBinding.ivRoomAudio.setOnClickListener(v ->
                ChatRoomManager.getInstance().toggleCloseRoomAudio()
        );

        if (isAnchorMode) {
            mViewBinding.ivRoomMute.setVisibility(View.VISIBLE);
            mViewBinding.ivRoomMore.setVisibility(View.VISIBLE);
        }
        mViewBinding.ivRoomMute.setOnClickListener(view -> {
            ToastUtils.showShort("禁言列表");
            //RoomMuteListDialog dialog = new RoomMuteListDialog(mActivity, mVoiceRoomInfo);
            //dialog.show(mActivity.getSupportFragmentManager(), dialog.getTag());
        });
        mViewBinding.ivRoomMore.setOnClickListener(view -> {

        });

        mViewBinding.etInputText.setOnEditorActionListener((v, actionId, event) -> {
            InputUtils.hideSoftInput(mActivity, mViewBinding.etInputText);
            String content = mViewBinding.etInputText.getText().toString().trim();
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
        mViewBinding.recyclerviewSeat.setLayoutManager(mSeatLayoutManager);

        mRoomSeatAdapter = new RoomSeatAdapter();
        mViewBinding.recyclerviewSeat.setAdapter(mRoomSeatAdapter);
        mRoomSeatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });

        mMsgLayoutManager = new LinearLayoutManager(mActivity);
        mViewBinding.recyclerviewMessage.setLayoutManager(mMsgLayoutManager);

        mMsgAdapter = new RoomMessageAdapter();
        mViewBinding.recyclerviewMessage.setAdapter(mMsgAdapter);
    }

    @Override
    public void onEnterRoom(boolean success) {
        if (!success) {
            ToastUtils.showShort("进入聊天室失败");
            mActivity.finish();
        }
    }

    @Override
    public void onLeaveRoom() {
        mActivity.finish();
    }

    @Override
    public void onRoomDismiss() {
        new AlertDialog.Builder(mActivity)
                .setTitle("通知")
                .setMessage("该房间已被主播解散")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChatRoomManager.getInstance().toggleLeaveRoom();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onOnlineUserCount(int onlineUserCount) {
        String count = "在线" + onlineUserCount + "人";
        mViewBinding.tvRoomMemberCount.setText(count);
    }

    @Override
    public void onLocalMuteMic(boolean muted) {
        mViewBinding.ivRoomMic.setSelected(muted);
    }

    @Override
    public void onRoomMuteAudio(boolean muted) {
        mViewBinding.ivRoomAudio.setSelected(muted);
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