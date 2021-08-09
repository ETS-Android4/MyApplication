package com.netease.audioroom.demo.activity;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.RoomMessageAdapter;
import com.netease.audioroom.demo.adapter.RoomSeatAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.dialog.RoomMuteListDialog;
import com.netease.audioroom.demo.util.InputUtils;
import com.netease.audioroom.demo.util.ViewUtils;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomMessage;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;
import com.netease.audioroom.demo.voiceroom.interfaces.NERtcVoiceRoomDef;
import com.netease.audioroom.demo.widget.HeadImageView;

import java.util.List;

/**
 * 主播与观众基础页，包含所有的通用UI元素
 */
public abstract class BaseRoomActivity extends BaseActivity implements NERtcVoiceRoomDef.RoomCallback {

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
    protected RoomSeatAdapter mRoomSeatAdapter;
    protected GridLayoutManager mSeatLayoutManager;

    //消息列表
    protected RecyclerView mMsgRecyclerView;
    protected RoomMessageAdapter mMsgAdapter;
    protected LinearLayoutManager mMsgLayoutManager;

    //底部操作栏按钮
    protected ImageView close, audio, mic, mute, more;

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

    /**
     * 屏蔽返回按键
     */
    @Override
    public void onBackPressed() {

    }

    private void initView() {
        View baseAudioView = findViewById(R.id.base_audio);
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
        tvRoomName = baseAudioView.findViewById(R.id.tv_room_name);
        tvAnnouncement = baseAudioView.findViewById(R.id.tv_room_announcement);
        tvAnnouncement.setOnClickListener(v ->
                ToastUtils.showShort("公告")
        );
        tvMemberCount = baseAudioView.findViewById(R.id.tv_room_member_count);

        close = baseAudioView.findViewById(R.id.iv_leave_room);

        //底部操作栏

        //扬声器
        audio = baseAudioView.findViewById(R.id.iv_room_audio);
        audio.setOnClickListener(v ->
                ChatRoomHelper.closeRoomAudio()
        );

        //麦克
        mic = baseAudioView.findViewById(R.id.iv_room_mic);
        mic.setOnClickListener(view ->
                ChatRoomHelper.closeMic()
        );
        //禁言
        mute = findViewById(R.id.iv_room_mute);
        mute.setOnClickListener(view -> {
                    RoomMuteListDialog dialog = new RoomMuteListDialog(this, mVoiceRoomInfo);
                    dialog.show(getSupportFragmentManager(), dialog.getTag());
                }
        );
        //     -> 管理员可见
        if (TextUtils.equals(DemoCache.getAccountId(), mVoiceRoomInfo.getCreatorAccount())) {
            mute.setVisibility(View.VISIBLE);
        }
        more = findViewById(R.id.iv_room_more);
        //     -> 管理员可见
        if (TextUtils.equals(DemoCache.getAccountId(), mVoiceRoomInfo.getCreatorAccount())) {
            more.setVisibility(View.VISIBLE);
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

        mSeatLayoutManager = new GridLayoutManager(this, 4);
        mSeatLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 4 : 1;
            }
        });
        mSeatRecyclerView.setLayoutManager(mSeatLayoutManager);
        mRoomSeatAdapter = new RoomSeatAdapter();
        mSeatRecyclerView.setAdapter(mRoomSeatAdapter);
        mRoomSeatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                onSeatItemClick((VoiceRoomSeat) adapter.getData().get(position), position);
            }
        });

        mMsgLayoutManager = new LinearLayoutManager(this);
        mMsgRecyclerView.setLayoutManager(mMsgLayoutManager);
        mMsgAdapter = new RoomMessageAdapter();
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
        new AlertDialog.Builder(BaseRoomActivity.this)
                .setTitle("通知")
                .setMessage("该房间已被主播解散")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChatRoomHelper.leaveRoom();
                        if (mVoiceRoomInfo.isSupportCDN()) {
                            finish();
                        }
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    /**
     * 主播信息更新
     *
     * @param user {@link VoiceRoomSeat 资料信息}
     */
    @Override
    public void onAnchorInfo(VoiceRoomUser user) {
        ivAnchorAvatar.loadAvatar(user.getAvatar());
        tvAnchorNick.setText(user.getNick());
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
        mRoomSeatAdapter.setNewInstance(seats);
    }

    /**
     * 更新麦位信息
     *
     * @param seat {@link VoiceRoomSeat 麦位}
     */
    @Override
    public void onUpdateSeat(VoiceRoomSeat seat) {
        mRoomSeatAdapter.notifyItemChanged(seat.getIndex(), seat);
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
            mSeatRecyclerView = findViewById(R.id.recyclerview_seat);
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
        mMsgAdapter.addData(message);
        mMsgLayoutManager.scrollToPosition(mMsgAdapter.getItemCount() - 1);
    }

    /**
     * 是否关闭麦克
     *
     * @param muted
     */
    @Override
    public void onLocalMuteMic(boolean muted) {
        mic.setSelected(muted);
    }

    /**
     * 是否关闭音频
     *
     * @param muted
     */
    @Override
    public void onRoomMuteAudio(boolean muted) {
        audio.setSelected(muted);
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
