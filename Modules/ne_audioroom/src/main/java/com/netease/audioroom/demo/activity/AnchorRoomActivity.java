package com.netease.audioroom.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.dialog.ChoiceDialog;
import com.netease.audioroom.demo.dialog.ListItemDialog;
import com.netease.audioroom.demo.dialog.MemberSelectDialog;
import com.netease.audioroom.demo.dialog.SeatApplyDialog;
import com.netease.audioroom.demo.dialog.TopTipsDialog;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.yunxin.android.lib.network.common.BaseResponse;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Status;
import com.netease.yunxin.nertc.model.interfaces.Anchor;
import com.netease.yunxin.nertc.util.SuccessCallback;

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

    private TopTipsDialog mTopTipsDialog;

    private TextView tvApplyHint;
    private SeatApplyDialog mSeatApplyDialog;//上麦请求列表

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
                if (mTopTipsDialog != null) {
                    mTopTipsDialog.dismiss();
                }
                loadSuccess();
            } else {
                Bundle bundle = new Bundle();
                TopTipsDialog.Style style = new TopTipsDialog.Style(getString(R.string.network_broken), 0, R.drawable.neterrricon, 0);
                bundle.putParcelable(mTopTipsDialog.TAG, style);
                mTopTipsDialog.setArguments(bundle);
                if (!mTopTipsDialog.isVisible()) {
                    mTopTipsDialog.show(getSupportFragmentManager(), mTopTipsDialog.TAG);
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
                new ChoiceDialog(AnchorRoomActivity.this)
                        .setTitle("确认结束直播？")
                        .setContent("请确认是否结束直播")
                        .setNegative(getString(R.string.cancel), null)
                        .setPositive("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onSeatAction(null, "退出房间");
                            }
                        })
                        .show();
            }
        });
        mTopTipsDialog = new TopTipsDialog();
        tvApplyHint = findViewById(R.id.apply_hint);
        tvApplyHint.setVisibility(View.INVISIBLE);
        tvApplyHint.setClickable(true);
        tvApplyHint.setOnClickListener(view -> {
                    mSeatApplyDialog = new SeatApplyDialog();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(mSeatApplyDialog.TAG, new ArrayList<>(ChatRoomHelper.getApplySeats()));
                    mSeatApplyDialog.setArguments(bundle);
                    mSeatApplyDialog.show(getSupportFragmentManager(), mSeatApplyDialog.TAG);
                    mSeatApplyDialog.setRequestAction(new SeatApplyDialog.IRequestAction() {
                        @Override
                        public void refuse(VoiceRoomSeat seat) {
                            //拒绝麦位申请
                            ChatRoomHelper.denySeatApply(seat);
                        }

                        @Override
                        public void agree(VoiceRoomSeat seat) {
                            //同意麦位申请
                            ChatRoomHelper.agreeSeatApply(seat);
                        }

                        @Override
                        public void dismiss() {

                        }
                    });
                }
        );
    }

    @Override
    protected void onSeatItemClick(VoiceRoomSeat seat, int position) {
        if (seat.getStatus() == Status.APPLY) {
            ToastHelper.showToast(getString(R.string.applying_now));
            return;
        }
        List<String> items = new ArrayList<>();
        ListItemDialog itemDialog = new ListItemDialog(AnchorRoomActivity.this);
        switch (seat.getStatus()) {
            // 抱观众上麦（点击麦位）
            case Status.INIT:
                items.add("将成员抱上麦位");
                items.add("屏蔽麦位");
                items.add("关闭麦位");
                break;
            // 当前存在有效用户
            case Status.ON:
                // 当前麦位已经关闭
            case Status.AUDIO_CLOSED:
                items.add("将TA踢下麦位");
                items.add("屏蔽麦位");
                break;
            // 当前麦位已经被关闭
            case Status.CLOSED:
                items.add("打开麦位");
                break;
            // 且当前麦位无人，麦位禁麦触发
            case Status.FORBID:
                items.add("将成员抱上麦位");
                items.add("解除语音屏蔽");
                break;
            // 当前麦位已经禁麦或已经关闭
            case Status.AUDIO_MUTED:
            case Status.AUDIO_CLOSED_AND_MUTED:
                items.add("将TA踢下麦位");
                items.add("解除语音屏蔽");
                break;
        }
        items.add(getString(R.string.cancel));
        itemDialog.setOnItemClickListener(item -> {
            onSeatAction(seat, item);
        }).show(items);
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
            if (mSeatApplyDialog != null && mSeatApplyDialog.isVisible()) {
                mSeatApplyDialog.update(seats);
            }
        } else {
            if (mSeatApplyDialog != null && mSeatApplyDialog.isVisible()) {
                mSeatApplyDialog.dismiss();
            }
        }
    }

    //
    // Anchor.Callback END
    //

    /**
     * 麦位菜单
     */
    private void onSeatAction(VoiceRoomSeat seat, String item) {
        switch (item) {
            case "将成员抱上麦位":
                //获取成员列表
                ChatRoomHelper.fetchMemberList(new SuccessCallback<List<VoiceRoomSeat>>() {
                    @Override
                    public void onSuccess(List<VoiceRoomSeat> seats) {
                        //展示成员列表
                        new MemberSelectDialog(AnchorRoomActivity.this, seats, member -> {
                            //被抱用户
                            if (member != null) {
                                ChatRoomHelper.checkIsRoomMember(seat.getIndex(), member);
                            }
                        }).show();

                    }
                });
                break;
            case "将TA踢下麦位":
                ChatRoomHelper.kickSeat(seat);
                break;
            case "关闭麦位":
                ChatRoomHelper.closeSeat(seat);
                break;
            case "屏蔽麦位":
                ChatRoomHelper.muteSeat(seat);
                break;
            case "解除语音屏蔽":
            case "打开麦位":
                ChatRoomHelper.openSeat(seat);
                break;
            case "退出房间":
                ChatRoomHelper.leaveRoom();
                break;
        }
    }

    @Override
    public void onLeaveRoom() {
        ChatRoomHttpClient.getInstance().closeRoom(DemoCache.getAccountId(),
                mVoiceRoomInfo.getRoomId(), new ChatRoomHttpClient.ChatRoomHttpCallback<BaseResponse<Void>>() {

                    @Override
                    public void onSuccess(BaseResponse<Void> response) {
                        loadSuccess();
                        ToastHelper.showToast("退出房间成功");
                        finish();
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        if (!Network.getInstance().isConnected()) {
                            ToastHelper.showToast("网络问题导致房间解散失败");
                        } else {
                            ToastHelper.showToast("房间解散失败 " + errorMsg);
                        }
                        finish();
                    }
                });
    }
}
