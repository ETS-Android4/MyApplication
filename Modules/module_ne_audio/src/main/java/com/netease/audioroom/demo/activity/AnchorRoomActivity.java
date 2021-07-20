package com.netease.audioroom.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.SeatApplyDialogFragment;
import com.netease.audioroom.demo.dialog.RoomSeatListDialog;
import com.netease.audioroom.demo.dialog.SeatMenuDialog;
import com.netease.audioroom.demo.dialog.TopTipsDialogFragment;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.yunxin.android.lib.network.common.BaseResponse;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Status;
import com.netease.yunxin.nertc.model.interfaces.Anchor;

import java.util.ArrayList;
import java.util.List;

/**
 * 主播页
 */
public class AnchorRoomActivity extends BaseRoomActivity implements Anchor.Callback {

    public static void start(Context context, VoiceRoomInfo voiceRoomInfo) {
        Intent intent = new Intent(context, AnchorRoomActivity.class);
        intent.putExtra(EXTRA_VOICE_ROOM_INFO, voiceRoomInfo);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        }
    }

    private TopTipsDialogFragment mTopTipsDialogFragment;

    private TextView tvApplyHint;
    private SeatApplyDialogFragment mSeatApplyDialogFragment;//上麦请求列表

    @Override
    protected int getContentViewID() {
        return R.layout.activity_anchor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatRoomHelper.enterRoom(true);

        watchNetWork();
    }

    private void watchNetWork() {
        NetworkChange.getInstance().getNetworkLiveData().observeInitAware(this, network -> {
            if (network != null && network.isConnected()) {
                if (mTopTipsDialogFragment != null) {
                    mTopTipsDialogFragment.dismiss();
                }
                loadSuccess();
            } else {
                Bundle bundle = new Bundle();
                TopTipsDialogFragment.Style style = new TopTipsDialogFragment.Style(getString(R.string.network_broken), 0, R.drawable.neterrricon, 0);
                bundle.putParcelable(mTopTipsDialogFragment.getTag(), style);
                mTopTipsDialogFragment.setArguments(bundle);
                if (!mTopTipsDialogFragment.isVisible()) {
                    mTopTipsDialogFragment.show(getSupportFragmentManager(), mTopTipsDialogFragment.getTag());
                }
                showError();
            }
        });
    }

    @Override
    protected void setupRoom() {
        // 初始化主播
        ChatRoomHelper.initAnchor(this);
    }

    @Override
    protected void setupView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AnchorRoomActivity.this)
                        .setTitle("确认结束直播？")
                        .setMessage("请确认是否结束直播")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ChatRoomHelper.leaveRoom();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            }
        });
        mTopTipsDialogFragment = new TopTipsDialogFragment();
        tvApplyHint = findViewById(R.id.apply_hint);
        tvApplyHint.setVisibility(View.INVISIBLE);
        tvApplyHint.setClickable(true);
        tvApplyHint.setOnClickListener(view -> {
                    //申请上麦弹窗
                    RoomSeatListDialog dialog = new RoomSeatListDialog();
                    dialog.show(getSupportFragmentManager(), dialog.getTag());
//                    mSeatApplyDialogFragment = new SeatApplyDialogFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList(mSeatApplyDialogFragment.getTag(), new ArrayList<>(ChatRoomHelper.getApplySeats()));
//                    mSeatApplyDialogFragment.setArguments(bundle);
//                    mSeatApplyDialogFragment.show(getSupportFragmentManager(), mSeatApplyDialogFragment.getTag());
//                    mSeatApplyDialogFragment.setRequestAction(new SeatApplyDialogFragment.IRequestAction() {
//                        @Override
//                        public void refuse(VoiceRoomSeat seat) {
//                            //拒绝麦位申请
//                            ChatRoomHelper.denySeatApply(seat);
//                        }
//
//                        @Override
//                        public void agree(VoiceRoomSeat seat) {
//                            //同意麦位申请
//                            ChatRoomHelper.agreeSeatApply(seat);
//                        }
//
//                        @Override
//                        public void dismiss() {
//
//                        }
//                    });
                }
        );
    }

    @Override
    protected void onSeatItemClick(VoiceRoomSeat seat, int position) {
        if (seat.getStatus() == Status.APPLY) {
            ToastUtils.showShort(getString(R.string.applying_now));
        } else {
            SeatMenuDialog dialog = new SeatMenuDialog(this, seat, createMenuItem(seat));
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        }
    }

    //
    // Anchor.Callback
    //

    /**
     * 上麦请求列表
     *
     * @param seats {@link VoiceRoomSeat 麦位列表}
     */
    @Override
    public void onApplySeats(List<VoiceRoomSeat> seats) {
        int size = seats.size();
        if (size > 0) {
            tvApplyHint.setVisibility(View.VISIBLE);
            tvApplyHint.setText(getString(R.string.apply_micro_has_arrow, size));
        } else {
            tvApplyHint.setVisibility(View.INVISIBLE);
        }
        if (size > 0) {
            if (mSeatApplyDialogFragment != null && mSeatApplyDialogFragment.isVisible()) {
                mSeatApplyDialogFragment.update(seats);
            }
        } else {
            if (mSeatApplyDialogFragment != null && mSeatApplyDialogFragment.isVisible()) {
                mSeatApplyDialogFragment.dismiss();
            }
        }
    }

    //
    // Anchor.Callback END
    //

    @Override
    public void onLeaveRoom() {
        ChatRoomHttpClient.getInstance().closeRoom(DemoCache.getAccountId(),
                mVoiceRoomInfo.getRoomId(), new ChatRoomHttpClient.ChatRoomHttpCallback<BaseResponse<Void>>() {

                    @Override
                    public void onSuccess(BaseResponse<Void> response) {
                        loadSuccess();
                        ToastUtils.showShort("退出房间成功");
                        finish();
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        if (!Network.getInstance().isConnected()) {
                            ToastUtils.showShort("网络问题导致房间解散失败");
                        } else {
                            ToastUtils.showShort("房间解散失败 " + errorMsg);
                        }
                        finish();
                    }
                });
    }

    private List<String> createMenuItem(VoiceRoomSeat seat) {
        List<String> menus = new ArrayList<>();
        switch (seat.getStatus()) {
            // 抱观众上麦（点击麦位）
            case VoiceRoomSeat.Status.INIT:
                menus.add("将成员抱上麦位");
                menus.add("屏蔽麦位");
                menus.add("关闭麦位");
                break;
            // 当前存在有效用户
            case VoiceRoomSeat.Status.ON:
                // 当前麦位已经关闭
            case VoiceRoomSeat.Status.AUDIO_CLOSED:
                menus.add("将TA踢下麦位");
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
}
