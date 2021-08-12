package com.netease.audioroom.demo.act;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.RoomMessageAdapter;
import com.netease.audioroom.demo.adapter.RoomSeatAdapter;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.databinding.ActivityBaseAudioBinding;
import com.netease.audioroom.demo.dialog.SeatMenuDialog;
import com.netease.audioroom.demo.util.InputUtils;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomMessage;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements IChatRoomCallback {

    public static final String EXTRA_VOICE_ROOM_INFO = "extra_voice_room_info";
    public static final String EXTRA_VOICE_ANCHOR_MODE = "extra_voice_anchor_mode";

    public static void start(Context context, VoiceRoomInfo info, boolean anchorMode) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra(EXTRA_VOICE_ROOM_INFO, info);
        intent.putExtra(EXTRA_VOICE_ANCHOR_MODE, anchorMode);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        }
    }

    private boolean isAnchorMode;
    private VoiceRoomInfo mVoiceRoomInfo;

    public ActivityBaseAudioBinding mBinding;

    protected RoomSeatAdapter mRoomSeatAdapter;
    protected GridLayoutManager mSeatLayoutManager;

    protected RoomMessageAdapter mMsgAdapter;
    protected LinearLayoutManager mMsgLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base_audio);

        Log.e("TAG", "ChatRoomActivity");

        // 屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBinding = ActivityBaseAudioBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        isAnchorMode = getIntent().getBooleanExtra(EXTRA_VOICE_ANCHOR_MODE, false);
        mVoiceRoomInfo = (VoiceRoomInfo) getIntent().getSerializableExtra(EXTRA_VOICE_ROOM_INFO);

        ChatRoomManager.getInstance().onActivityAttach(this);

        //聊天室初始化
        ChatRoomManager.getInstance().initRoom(this, mVoiceRoomInfo);

        initView();

        //进入直播间
        ChatRoomManager.getInstance().enterRoom(isAnchorMode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatRoomManager.getInstance().onActivityDetach();
    }

    private void initView() {
        mBinding.ivLeaveRoom.setOnClickListener(v -> {
                    if (isAnchorMode) {
                        new AlertDialog.Builder(this)
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
                        finish();
                    }
                }
        );

        String name = mVoiceRoomInfo.getName();
        name = TextUtils.isEmpty(name) ? mVoiceRoomInfo.getRoomId() : name;
        mBinding.tvRoomName.setText(name);

        String count = "在线" + mVoiceRoomInfo.getOnlineUserCount() + "人";
        mBinding.tvRoomMemberCount.setText(count);

        mBinding.tvRoomAnnouncement.setOnClickListener(v ->
                ToastUtils.showShort("房间公告")
        );

        mBinding.ivRoomMic.setOnClickListener(view ->
                ChatRoomManager.getInstance().toggleCloseLocalMic()
        );
        mBinding.ivRoomAudio.setOnClickListener(v ->
                ChatRoomManager.getInstance().toggleCloseRoomAudio()
        );

        if (isAnchorMode) {
            mBinding.ivRoomMute.setVisibility(View.VISIBLE);
            mBinding.ivRoomMore.setVisibility(View.VISIBLE);
        }
        mBinding.ivRoomMute.setOnClickListener(view -> {
            ToastUtils.showShort("禁言列表");
            //RoomMuteListDialog dialog = new RoomMuteListDialog(mActivity, mVoiceRoomInfo);
            //dialog.show(mActivity.getSupportFragmentManager(), dialog.getTag());
        });
        mBinding.ivRoomMore.setOnClickListener(view -> {
            ToastUtils.showShort("更多菜单");
        });

        mBinding.etInputText.setOnEditorActionListener((v, actionId, event) -> {
            InputUtils.hideSoftInput(this, mBinding.etInputText);
            String content = mBinding.etInputText.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showShort("请输入消息内容");
            }
            return true;
        });

        mSeatLayoutManager = new GridLayoutManager(this, 4);
        mSeatLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 4 : 1;
            }
        });
        mBinding.recyclerviewSeat.setLayoutManager(mSeatLayoutManager);

        mRoomSeatAdapter = new RoomSeatAdapter();
        mBinding.recyclerviewSeat.setAdapter(mRoomSeatAdapter);
        mRoomSeatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                VoiceRoomSeat seat = (VoiceRoomSeat) adapter.getData().get(position);
                if (seat.getStatus() == VoiceRoomSeat.Status.APPLY) {
                    ToastUtils.showShort(getString(R.string.applying_now));
                } else {
                    SeatMenuDialog dialog;
                    if (isAnchorMode) {
                        dialog = new SeatMenuDialog(ChatRoomActivity.this, seat, createAnchorMenuItem(seat));
                    } else {
                        dialog = new SeatMenuDialog(ChatRoomActivity.this, seat, createAudienceMenuItem(seat));
                    }
                    dialog.show(getSupportFragmentManager(), dialog.getTag());
                }
            }
        });

        mMsgLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerviewMessage.setLayoutManager(mMsgLayoutManager);

        mMsgAdapter = new RoomMessageAdapter();
        mBinding.recyclerviewMessage.setAdapter(mMsgAdapter);
    }

    @Override
    public void onEnterRoom(boolean success) {
        if (!success) {
            ToastUtils.showShort("进入聊天室失败");
            finish();
        }
    }

    @Override
    public void onLeaveRoom() {
        finish();
    }

    @Override
    public void onRoomDismiss() {
        new AlertDialog.Builder(this)
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
    public void onAnchorInfo(VoiceRoomUser user) {
        mBinding.ivAnchorAvatar.loadAvatar(user.getAvatar());
        mBinding.tvAnchorNick.setText(user.getNick());
    }

    @Override
    public void onAnchorMute(boolean muted) {
        mBinding.ivAnchorAudio.setImageResource(muted ? R.drawable.icon_seat_close_micro : R.drawable.icon_mic);
    }

    @Override
    public void onAnchorVolume(int volume) {
        showVolume(mBinding.ivAnchorCircle, volume);
    }

    @Override
    public void onOnlineUserCount(int onlineUserCount) {
        String count = "在线" + onlineUserCount + "人";
        mBinding.tvRoomMemberCount.setText(count);
    }

    @Override
    public void onLocalMuteMic(boolean muted) {
        mBinding.ivRoomMic.setSelected(muted);
    }

    @Override
    public void onRoomMuteAudio(boolean muted) {
        mBinding.ivRoomAudio.setSelected(muted);
    }

    @Override
    public void onUpdateSeats(List<VoiceRoomSeat> seats) {
        mRoomSeatAdapter.setNewInstance(seats);
    }

    @Override
    public void onUpdateSeat(VoiceRoomSeat seat) {
        mRoomSeatAdapter.setData(seat.getIndex(), seat);
    }

    @Override
    public void onApplySeats(List<VoiceRoomSeat> seats) {

    }

    @Override
    public void onUpdateVolume(int index, int volume) {

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

    /**
     * 显示音量
     */
    private static void showVolume(ImageView view, int volume) {
        volume = toStepVolume(volume);
        if (volume == 0) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private static int toStepVolume(int volume) {
        int step = 0;
        volume /= 40;
        while (volume > 0) {
            step++;
            volume /= 2;
        }
        if (step > 8) {
            step = 8;
        }
        return step;
    }

    private List<String> createAnchorMenuItem(VoiceRoomSeat seat) {
        List<String> menus = new ArrayList<>();
        switch (seat.getStatus()) {
            // 抱观众上麦（点击麦位）
            case VoiceRoomSeat.Status.INIT:
                //menus.add("上麦");
                menus.add("申请上麦");
                menus.add("将成员抱上麦位");
                menus.add("屏蔽麦位");
                menus.add("关闭麦位");
                break;
            // 当前存在有效用户
            case VoiceRoomSeat.Status.ON:
                // 当前麦位已经关闭
            case VoiceRoomSeat.Status.AUDIO_CLOSED:
                if (TextUtils.equals(DemoCache.getAccountId(), seat.getAccount())) {
                    menus.add("下麦");
                } else {
                    menus.add("将TA踢下麦位");
                }
                menus.add("屏蔽麦位");
                break;
            // 当前麦位已经被关闭
            case VoiceRoomSeat.Status.CLOSED:
                menus.add("打开麦位");
                break;
            // 且当前麦位无人，麦位禁麦触发
            case VoiceRoomSeat.Status.FORBID:
                menus.add("将成员抱上麦位");
                menus.add("解除语音屏蔽");
                break;
            // 当前麦位已经禁麦或已经关闭
            case VoiceRoomSeat.Status.AUDIO_MUTED:
            case VoiceRoomSeat.Status.AUDIO_CLOSED_AND_MUTED:
                menus.add("将TA踢下麦位");
                menus.add("解除语音屏蔽");
                break;
        }
        menus.add(getString(R.string.cancel));
        return menus;
    }

    private List<String> createAudienceMenuItem(VoiceRoomSeat seat) {
        List<String> menus = new ArrayList<>();
        switch (seat.getStatus()) {
            // 抱观众上麦（点击麦位）
            case VoiceRoomSeat.Status.INIT:
                //menus.add("上麦");
                //menus.add("申请上麦");
                break;
            // 当前存在有效用户
            case VoiceRoomSeat.Status.ON:
                // 当前麦位已经关闭
            case VoiceRoomSeat.Status.AUDIO_CLOSED:
                // 当前麦位已经被关闭
            case VoiceRoomSeat.Status.CLOSED:
                // 且当前麦位无人，麦位禁麦触发
            case VoiceRoomSeat.Status.FORBID:
                // 当前麦位已经禁麦或已经关闭
            case VoiceRoomSeat.Status.AUDIO_MUTED:
            case VoiceRoomSeat.Status.AUDIO_CLOSED_AND_MUTED:
                break;
        }
        menus.add(getString(R.string.cancel));
        return menus;
    }
}