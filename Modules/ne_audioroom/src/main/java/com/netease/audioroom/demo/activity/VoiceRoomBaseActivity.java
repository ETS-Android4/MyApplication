package com.netease.audioroom.demo.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.MessageListAdapter;
import com.netease.audioroom.demo.adapter.SeatAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.dialog.ChatRoomMoreDialog;
import com.netease.audioroom.demo.dialog.ChoiceDialog;
import com.netease.audioroom.demo.dialog.MuteMemberDialog;
import com.netease.audioroom.demo.dialog.NoticeDialog;
import com.netease.audioroom.demo.dialog.NotificationDialog;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.util.InputUtils;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.ScreenUtil;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.audioroom.demo.util.ViewUtils;
import com.netease.audioroom.demo.widget.HeadImageView;
import com.netease.audioroom.demo.widget.VerticalItemDecoration;
import com.netease.yunxin.nertc.nertcvoiceroom.model.NERtcVoiceRoom;
import com.netease.yunxin.nertc.nertcvoiceroom.model.NERtcVoiceRoomDef.RoomCallback;
import com.netease.yunxin.nertc.nertcvoiceroom.model.VoiceRoomInfo;
import com.netease.yunxin.nertc.nertcvoiceroom.model.VoiceRoomMessage;
import com.netease.yunxin.nertc.nertcvoiceroom.model.VoiceRoomSeat;
import com.netease.yunxin.nertc.nertcvoiceroom.model.VoiceRoomUser;

import java.util.Collections;
import java.util.List;

/**
 * 主播与观众基础页，包含所有的通用UI元素
 */
public abstract class VoiceRoomBaseActivity extends BaseActivity implements RoomCallback {

    public static final String TAG = "AudioRoom";

    public static final String EXTRA_VOICE_ROOM_INFO = "extra_voice_room_info";

    protected NERtcVoiceRoom voiceRoom;

    //主播信息
    protected ConstraintLayout mAnchorView;
    protected HeadImageView ivAnchorAvatar;
    protected ImageView ivAnchorCircle;
    protected ImageView ivAnchorAudio;
    protected TextView tvAnchorNick;

    //房间信息
    protected TextView tvRoomName;
    protected View tvAnnouncement;
    protected TextView tvMemberCount;

    //麦位
    protected RecyclerView mSeatRecyclerView;
    protected SeatAdapter mSeatAdapter;

    //消息列表
    protected RecyclerView mMsgRecyclerView;
    protected MessageListAdapter mMsgAdapter;
    protected LinearLayoutManager mMsgLayoutManager;

    //底部操作栏按钮
    protected ImageView close, audio, mute, more;

    protected TextView tvInput;
    protected EditText edtInput;

    protected VoiceRoomInfo voiceRoomInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        voiceRoomInfo = (VoiceRoomInfo) getIntent().getSerializableExtra(EXTRA_VOICE_ROOM_INFO);
        if (voiceRoomInfo == null) {
            ToastHelper.showToast("聊天室信息不能为空");
            finish();
            return;
        }
        initVoiceRoom();
        initBaseView();
        setupBaseView();
        requestLivePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Network.getInstance().isConnected()) {
            loadSuccess();
        } else {
            showNetError();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doLeaveRoom();
    }

    private void initBaseView() {
        View baseAudioView = findViewById(R.id.rl_base_audio_ui);
        if (baseAudioView == null) {
            throw new IllegalStateException("xml layout must include base_audio_ui.xml layout");
        }

        // 主播信息
        mAnchorView = baseAudioView.findViewById(R.id.cly_anchor_layout);
        ivAnchorAvatar = baseAudioView.findViewById(R.id.iv_liver_avatar);
        ivAnchorCircle = baseAudioView.findViewById(R.id.circle);
        ivAnchorAudio = baseAudioView.findViewById(R.id.iv_liver_audio);
        tvAnchorNick = baseAudioView.findViewById(R.id.tv_liver_nick);

        // 房间信息
        tvRoomName = baseAudioView.findViewById(R.id.tv_chat_room_name);
        tvAnnouncement = baseAudioView.findViewById(R.id.tv_room_announcement);
        tvAnnouncement.setOnClickListener(v -> {
            new NoticeDialog()
                    .show(getSupportFragmentManager(), "notice");
        });
        tvMemberCount = baseAudioView.findViewById(R.id.tv_room_member_count);

        close = baseAudioView.findViewById(R.id.iv_leave_room);
        close.setOnClickListener(view ->
                doLeaveRoom()
        );

        //底部操作栏
        //麦克
        audio = baseAudioView.findViewById(R.id.iv_room_audio);
        audio.setOnClickListener(view ->
                toMuteAudio()
        );
        //禁言
        mute = findViewById(R.id.iv_room_mute);
        mute.setOnClickListener(view ->
                new MuteMemberDialog(VoiceRoomBaseActivity.this, voiceRoomInfo).show()
        );

        //更多
        more = baseAudioView.findViewById(R.id.iv_room_more);
        more.setOnClickListener(v ->
                new ChatRoomMoreDialog(VoiceRoomBaseActivity.this, getMoreItems())
                        .registerOnItemClickListener(getMoreItemClickListener())
                        .show()
        );

        mSeatRecyclerView = baseAudioView.findViewById(R.id.recyclerview_seat);
        mMsgRecyclerView = baseAudioView.findViewById(R.id.rcy_chat_message_list);

        tvInput = baseAudioView.findViewById(R.id.tv_input_text);
        tvInput.setOnClickListener(v ->
                InputUtils.showSoftInput(VoiceRoomBaseActivity.this, edtInput)
        );
        edtInput = baseAudioView.findViewById(R.id.edt_input_text);
        edtInput.setOnEditorActionListener((v, actionId, event) -> {
            InputUtils.hideSoftInput(VoiceRoomBaseActivity.this, edtInput);
            String content = edtInput.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastHelper.showToast("请输入消息内容");
            }
            voiceRoom.sendTextMessage(content);
            return true;
        });

        String name = voiceRoomInfo.getName();
        name = TextUtils.isEmpty(name) ? voiceRoomInfo.getRoomId() : name;
        tvRoomName.setText(name);
        String count = "在线" + voiceRoomInfo.getOnlineUserCount() + "人";
        tvMemberCount.setText(count);

        mSeatRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mSeatAdapter = new SeatAdapter(null, this);

        mSeatRecyclerView.setAdapter(mSeatAdapter);
        mSeatAdapter.setItemClickListener(this::onSeatItemClick);
        mMsgLayoutManager = new LinearLayoutManager(this);
        mMsgRecyclerView.setLayoutManager(mMsgLayoutManager);
        mMsgAdapter = new MessageListAdapter(null, this);
        mMsgRecyclerView.addItemDecoration(new VerticalItemDecoration(Color.TRANSPARENT, ScreenUtil.dip2px(this, 5)));
        mMsgRecyclerView.setAdapter(mMsgAdapter);
    }

    @NonNull
    protected List<ChatRoomMoreDialog.MoreItem> getMoreItems() {
        return Collections.emptyList();
    }

    protected abstract int getContentViewID();

    protected abstract void setupBaseView();

    protected abstract void onSeatItemClick(VoiceRoomSeat model, int position);

    protected ChatRoomMoreDialog.OnItemClickListener getMoreItemClickListener() {
        return onMoreItemClickListener;
    }

    //
    // NERtcVoiceRoom call
    //

    /**
     * 初始化直播间
     */
    protected void initVoiceRoom() {
        NERtcVoiceRoom.setAccountMapper(AccountInfo::accountUid);
        NERtcVoiceRoom.setMessageTextBuilder(messageTextBuilder);
        voiceRoom = NERtcVoiceRoom.sharedInstance(this);
        voiceRoom.init(BuildConfig.NERTC_APP_KEY, this);
        voiceRoom.initRoom(voiceRoomInfo, createUser());
    }

    /**
     * 进入直播间
     */
    protected final void enterRoom(boolean anchorMode) {
        voiceRoom.enterRoom(anchorMode);
    }

    /**
     * 离开直播间
     */
    protected void doLeaveRoom() {
        voiceRoom.leaveRoom();
    }

    /**
     * 静音
     */
    protected final void toMuteAudio() {
        boolean muted = voiceRoom.muteLocalAudio(!voiceRoom.isLocalAudioMute());
        if (muted) {
            ToastHelper.showToast("话筒已关闭");
        } else {
            ToastHelper.showToast("话筒已打开");
        }
    }

    //
    // RoomCallback
    //
    @Override
    public void onEnterRoom(boolean success) {
        if (!success) {
            ToastHelper.showToast("进入聊天室失败");
            finish();
        } else {
            loadSuccess();
        }
    }

    @Override
    public void onLeaveRoom() {
        finish();
    }

    @Override
    public void onRoomDismiss() {
        ChoiceDialog dialog = new NotificationDialog(this)
                .setTitle("通知")
                .setContent("该房间已被主播解散")
                .setPositive("知道了", v -> {
                    doLeaveRoom();
                    if (voiceRoomInfo.isSupportCDN()) {
                        finish();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onAnchorInfo(VoiceRoomUser user) {
        ivAnchorAvatar.loadAvatar(user.avatar);
        tvAnchorNick.setText(user.nick);
    }

    @Override
    public void onAnchorMute(boolean muted) {
        ivAnchorAudio.setImageResource(muted ? R.drawable.icon_seat_close_micro : R.drawable.icon_seat_open_micro);
    }

    @Override
    public void onAnchorVolume(int volume) {
        showVolume(ivAnchorCircle, volume);
    }

    @Override
    public void onOnlineUserCount(int onlineUserCount) {
        String count = "在线" + onlineUserCount + "人";
        tvMemberCount.setText(count);
    }

    @Override
    public void onMuteLocalAudio(boolean muted) {
        audio.setSelected(muted);
    }

    @Override
    public void updateSeats(List<VoiceRoomSeat> seats) {
        mSeatAdapter.setItems(seats);
    }

    @Override
    public void updateSeat(VoiceRoomSeat seat) {
        mSeatAdapter.updateItem(seat.getIndex(), seat);
    }

    @Override
    public void onSeatVolume(VoiceRoomSeat seat, int volume) {
        if (mSeatRecyclerView == null) {
            mSeatRecyclerView = findViewById(R.id.rl_base_audio_ui).findViewById(R.id.recyclerview_seat);
        }
        if (mSeatRecyclerView.getLayoutManager() == null) {
            mSeatRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }
        View itemView = mSeatRecyclerView.getLayoutManager().findViewByPosition(seat.getIndex());
        if (itemView != null) {
            ImageView circle = itemView.findViewById(R.id.circle);
            showVolume(circle, volume);
        }
    }

    @Override
    public void onVoiceRoomMessage(VoiceRoomMessage message) {
        mMsgAdapter.appendItem(message);
        mMsgLayoutManager.scrollToPosition(mMsgAdapter.getItemCount() - 1);
    }

    /**
     * 显示麦位音量
     *
     * @param view
     * @param volume
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

    private static final VoiceRoomMessage.MessageTextBuilder messageTextBuilder =
            new VoiceRoomMessage.MessageTextBuilder() {

                @Override
                public String roomEvent(String nick, boolean enter) {
                    String who = "“" + nick + "”";
                    String action = enter ? "进了房间" : "离开了房间";
                    return who + action;
                }

                @Override
                public String seatEvent(VoiceRoomSeat seat, boolean enter) {
                    VoiceRoomUser user = seat.getUser();
                    String nick = user != null ? user.getNick() : "";
                    String who = "“" + nick + "”";
                    String action = enter ? "进入了麦位" : "退出了麦位";
                    int position = seat.getIndex() + 1;
                    return who + action + position;
                }
            };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        // 键盘区域外点击收起键盘
        if (!ViewUtils.isInView(edtInput, x, y)) {
            InputUtils.hideSoftInput(VoiceRoomBaseActivity.this, edtInput);
        }
        return super.dispatchTouchEvent(ev);
    }

    protected static final int MORE_ITEM_MICRO_PHONE = 0; //麦克风

    protected static final int MORE_ITEM_FINISH = 5 - 1; //关闭房间

    /**
     * 底部操作栏 - 更多菜单
     */
    protected ChatRoomMoreDialog.OnItemClickListener onMoreItemClickListener = new ChatRoomMoreDialog.OnItemClickListener() {
        @Override
        public boolean onItemClick(Dialog dialog, View itemView, ChatRoomMoreDialog.MoreItem item) {
            switch (item.id) {
                case MORE_ITEM_MICRO_PHONE: { //麦克风
                    item.enable = !voiceRoom.isLocalAudioMute();
                    toMuteAudio();
                    break;
                }
                case MORE_ITEM_FINISH: { //关闭房间
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    doLeaveRoom();
                    break;
                }
            }
            return true;
        }
    };
}
