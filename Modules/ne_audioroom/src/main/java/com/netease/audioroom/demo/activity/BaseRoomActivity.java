package com.netease.audioroom.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.MessageListAdapter;
import com.netease.audioroom.demo.adapter.SeatAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.dialog.MemberMuteBottomDialog;
import com.netease.audioroom.demo.dialog.NoticeDialog;
import com.netease.audioroom.demo.util.InputUtils;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.ViewUtils;
import com.netease.audioroom.demo.widget.HeadImageView;
import com.netease.yunxin.nertc.model.NERtcVoiceRoomDef.RoomCallback;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomMessage;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;

import java.util.List;

/**
 * 主播与观众基础页，包含所有的通用UI元素
 */
public abstract class BaseRoomActivity extends BaseActivity implements RoomCallback {

    public static final String EXTRA_VOICE_ROOM_INFO = "extra_voice_room_info";

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
    protected ImageView close, audio, mic, mute;

    //聊天框
    protected TextView tvInput;
    protected EditText edtInput;

    protected VoiceRoomInfo mVoiceRoomInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mVoiceRoomInfo = (VoiceRoomInfo) getIntent().getSerializableExtra(EXTRA_VOICE_ROOM_INFO);
        if (mVoiceRoomInfo == null) {
            ToastUtils.showShort("聊天室信息不能为空");
            finish();
            return;
        }
        //聊天室初始化
        ChatRoomHelper.initRoom(this, mVoiceRoomInfo, this);

        initView();

        setupRoom();
        setupView();

        requestLivePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Network.getInstance().isConnected()) {
            loadSuccess();
        } else {
            showError();
        }
    }

    /**
     * 屏蔽返回按键
     */
    @Override
    public void onBackPressed() {

    }

    private void initView() {
        View baseAudioView = findViewById(R.id.cl_base_audio_ui);
        if (baseAudioView == null) {
            throw new IllegalStateException("xml layout must include base_audio_ui.xml layout");
        }

        // 主播信息
        mAnchorView = baseAudioView.findViewById(R.id.cl_anchor_layout);
        ivAnchorAvatar = baseAudioView.findViewById(R.id.iv_liver_avatar);
        ivAnchorCircle = baseAudioView.findViewById(R.id.iv_circle);
        ivAnchorAudio = baseAudioView.findViewById(R.id.iv_liver_audio);
        tvAnchorNick = baseAudioView.findViewById(R.id.tv_liver_nick);

        // 房间信息
        tvRoomName = baseAudioView.findViewById(R.id.tv_chat_room_name);
        tvAnnouncement = baseAudioView.findViewById(R.id.tv_room_announcement);
        tvAnnouncement.setOnClickListener(v ->
                ToastUtils.showShort("公告")
        );
        tvMemberCount = baseAudioView.findViewById(R.id.tv_room_member_count);

        close = baseAudioView.findViewById(R.id.iv_leave_room);

        //底部操作栏

        //扬声器
        audio = baseAudioView.findViewById(R.id.iv_room_audio);

        //麦克
        mic = baseAudioView.findViewById(R.id.iv_room_mic);
        mic.setOnClickListener(view ->
                ChatRoomHelper.closeMic()
        );
        //禁言
        mute = findViewById(R.id.iv_room_mute);
        mute.setOnClickListener(view ->
                new MemberMuteBottomDialog(BaseRoomActivity.this, mVoiceRoomInfo)
                        .show()
        );
        //     -> 管理员可见
        if (TextUtils.equals(DemoCache.getAccountId(), mVoiceRoomInfo.getCreatorAccount())) {
            mute.setVisibility(View.VISIBLE);
        }

        mSeatRecyclerView = baseAudioView.findViewById(R.id.recyclerview_seat);
        mMsgRecyclerView = baseAudioView.findViewById(R.id.recyclerview_message);

        tvInput = baseAudioView.findViewById(R.id.tv_input_text);
        tvInput.setOnClickListener(v ->
                InputUtils.showSoftInput(BaseRoomActivity.this, edtInput)
        );
        edtInput = baseAudioView.findViewById(R.id.et_input_text);
        edtInput.setOnEditorActionListener((v, actionId, event) -> {
            InputUtils.hideSoftInput(BaseRoomActivity.this, edtInput);
            String content = edtInput.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showShort("请输入消息内容");
            }
            ChatRoomHelper.sendMessage(content);
            return true;
        });

        String name = mVoiceRoomInfo.getName();
        name = TextUtils.isEmpty(name) ? mVoiceRoomInfo.getRoomId() : name;
        tvRoomName.setText(name);
        String count = "在线" + mVoiceRoomInfo.getOnlineUserCount() + "人";
        tvMemberCount.setText(count);

        mSeatRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mSeatAdapter = new SeatAdapter(this);

        mSeatRecyclerView.setAdapter(mSeatAdapter);
        mSeatAdapter.setItemClickListener(this::onSeatItemClick);
        mMsgLayoutManager = new LinearLayoutManager(this);
        mMsgRecyclerView.setLayoutManager(mMsgLayoutManager);
        mMsgAdapter = new MessageListAdapter(null, this);
        mMsgRecyclerView.setAdapter(mMsgAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        // 键盘区域外点击收起键盘
        if (!ViewUtils.isInView(edtInput, x, y)) {
            InputUtils.hideSoftInput(BaseRoomActivity.this, edtInput);
        }
        return super.dispatchTouchEvent(ev);
    }

    protected abstract int getContentViewID();

    protected abstract void setupRoom();

    protected abstract void setupView();

    protected abstract void onSeatItemClick(VoiceRoomSeat seat, int position);

    //
    // RoomCallback
    //

    /**
     * 进入房间
     *
     * @param success 是否成功
     */
    @Override
    public void onEnterRoom(boolean success) {
        if (!success) {
            ToastUtils.showShort("进入聊天室失败");
            finish();
        } else {
            loadSuccess();
        }
    }

    /**
     * 离开房间
     */
    @Override
    public void onLeaveRoom() {
        finish();
    }

    /**
     * 房间解散
     */
    @Override
    public void onRoomDismiss() {
        NoticeDialog dialog = new NoticeDialog(this)
                .setTitle("通知")
                .setContent("该房间已被主播解散")
                .setPositive("知道了", v -> {
                    ChatRoomHelper.leaveRoom();
                    if (mVoiceRoomInfo.isSupportCDN()) {
                        finish();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 主播信息更新
     *
     * @param user {@link VoiceRoomSeat 资料信息}
     */
    @Override
    public void onAnchorInfo(VoiceRoomUser user) {
        ivAnchorAvatar.loadAvatar(user.avatar);
        tvAnchorNick.setText(user.nick);
    }


    /**
     * 主播静音状态
     *
     * @param muted 是否静音
     */
    @Override
    public void onAnchorMute(boolean muted) {
        ivAnchorAudio.setImageResource(muted ? R.drawable.icon_seat_close_micro : R.drawable.icon_mic);
    }

    /**
     * 主播说话音量
     *
     * @param volume 说话音量0-100
     */
    @Override
    public void onAnchorVolume(int volume) {
        showVolume(ivAnchorCircle, volume);
    }

    /**
     * 当前在线用户数量更新
     *
     * @param onlineUserCount 当前在线用户数量
     */
    @Override
    public void onOnlineUserCount(int onlineUserCount) {
        String count = "在线" + onlineUserCount + "人";
        tvMemberCount.setText(count);
    }

    /**
     * 更新所有麦位信息
     *
     * @param seats {@link VoiceRoomSeat 麦位}
     */
    @Override
    public void onUpdateSeats(List<VoiceRoomSeat> seats) {
        mSeatAdapter.setItems(seats);
    }

    /**
     * 更新麦位信息
     *
     * @param seat {@link VoiceRoomSeat 麦位}
     */
    @Override
    public void onUpdateSeat(VoiceRoomSeat seat) {
        mSeatAdapter.updateItem(seat.getIndex(), seat);
    }

    /**
     * 麦位音量
     *
     * @param seat   {@link VoiceRoomSeat 麦位}
     * @param volume 说话音量0-100
     */
    @Override
    public void onSeatVolume(VoiceRoomSeat seat, int volume) {
        if (mSeatRecyclerView == null) {
            mSeatRecyclerView = findViewById(R.id.cl_base_audio_ui).findViewById(R.id.recyclerview_seat);
        }
        if (mSeatRecyclerView.getLayoutManager() == null) {
            mSeatRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }
        View itemView = mSeatRecyclerView.getLayoutManager().findViewByPosition(seat.getIndex());
        if (itemView != null) {
            ImageView circle = itemView.findViewById(R.id.iv_liver_circle);
            showVolume(circle, volume);
        }
    }

    /**
     * 收到消息
     *
     * @param message {@link VoiceRoomMessage 消息}
     */
    @Override
    public void onVoiceRoomMessage(VoiceRoomMessage message) {
        mMsgAdapter.appendItem(message);
        mMsgLayoutManager.scrollToPosition(mMsgAdapter.getItemCount() - 1);
    }

    /**
     * ???
     *
     * @param muted 是否静音
     */
    @Override
    public void onMuteAudio(boolean muted) {
        mic.setSelected(muted);
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
}
