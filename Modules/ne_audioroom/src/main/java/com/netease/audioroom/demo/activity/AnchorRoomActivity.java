package com.netease.audioroom.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.dialog.ChoiceDialog;
import com.netease.audioroom.demo.dialog.ListItemDialog;
import com.netease.audioroom.demo.dialog.MemberSelectDialog;
import com.netease.audioroom.demo.dialog.RoomMoreDialog;
import com.netease.audioroom.demo.dialog.SeatApplyDialog;
import com.netease.audioroom.demo.dialog.TopTipsDialog;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.android.lib.network.common.BaseResponse;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Reason;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Status;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.model.interfaces.Anchor;
import com.netease.yunxin.nertc.util.SuccessCallback;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * 底部菜单栏
     */
    private static final List<RoomMoreDialog.MoreItem> MORE_ITEMS = Arrays.asList(
            new RoomMoreDialog.MoreItem(MORE_ITEM_MICRO_PHONE, R.drawable.selector_more_micro_phone_status, "麦克风"),
            new RoomMoreDialog.MoreItem(MORE_ITEM_FINISH, R.drawable.icon_room_more_finish, "结束")
    );

    @Override
    protected List<RoomMoreDialog.MoreItem> getMoreItems() {
        return MORE_ITEMS;
    }

    private TopTipsDialog mTopTipsDialog;
    private Anchor anchor;

    private TextView tvApplyHint;
    private SeatApplyDialog mSeatApplyDialog;

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
        anchor = ChatRoomHelper.getNERtcVoiceRoom().getAnchor();
        anchor.setCallback(this);
    }

    @Override
    protected void setupView() {
        mTopTipsDialog = new TopTipsDialog();
        tvApplyHint = findViewById(R.id.apply_hint);
        tvApplyHint.setVisibility(View.INVISIBLE);
        tvApplyHint.setClickable(true);
        tvApplyHint.setOnClickListener(view ->
                showApplySeats(anchor.getApplySeats())
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

    @Override
    protected void closeRoom() {
        new ChoiceDialog(AnchorRoomActivity.this)
                .setTitle("确认结束直播？")
                .setContent("请确认是否结束直播")
                .setNegative(getString(R.string.cancel), null)
                .setPositive("确认", v -> onSeatAction(null, "退出房间"))
                .show();
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
    // ==========
    //

    /**
     * 展示麦上用户
     *
     * @param seats
     */
    private void showApplySeats(List<VoiceRoomSeat> seats) {
        mSeatApplyDialog = new SeatApplyDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(mSeatApplyDialog.TAG, new ArrayList<>(seats));
        mSeatApplyDialog.setArguments(bundle);
        mSeatApplyDialog.show(getSupportFragmentManager(), mSeatApplyDialog.TAG);
        mSeatApplyDialog.setRequestAction(new SeatApplyDialog.IRequestAction() {
            @Override
            public void refuse(VoiceRoomSeat seat) {
                denySeatApply(seat);
            }

            @Override
            public void agree(VoiceRoomSeat seat) {
                approveSeatApply(seat);
            }

            @Override
            public void dismiss() {

            }
        });
    }

    /**
     * 拒绝麦位申请
     *
     * @param seat
     */
    private void denySeatApply(VoiceRoomSeat seat) {
        VoiceRoomUser user = seat.getUser();
        String nick = user != null ? user.getNick() : "";
        final String text = "已拒绝“" + nick + "”的申请";
        anchor.denySeatApply(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastHelper.showToast(text);
            }
        });
    }

    /**
     * 同意上麦申请
     *
     * @param seat
     */
    private void approveSeatApply(VoiceRoomSeat seat) {
        final String text = "成功通过连麦请求";

        boolean ret = anchor.approveSeatApply(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast(text);
            }
        });
        if (!ret) {
            denySeatApply(seat);
        }
    }

    /**
     * 麦位菜单
     *
     * @param seat
     * @param item
     */
    private void onSeatAction(VoiceRoomSeat seat, String item) {
        switch (item) {
            case "确定踢下麦位":
                new ListItemDialog(AnchorRoomActivity.this).setOnItemClickListener(item1 -> {
                    if ("确定踢下麦位".equals(item1)) {
                        kickSeat(seat);
                    }
                }).show(Arrays.asList("确定踢下麦位", "取消"));
                break;
            case "关闭麦位":
                closeSeat(seat);
                break;
            case "将成员抱上麦位":
                inviteSeat0(seat);
                break;
            case "将TA踢下麦位":
                kickSeat(seat);
                break;
            case "屏蔽麦位":
                muteSeat(seat);
                break;
            case "解除语音屏蔽":
            case "打开麦位":
                openSeat(seat);
                break;
            case "退出房间":
                ChatRoomHelper.leaveRoom();
                break;
        }
    }

    /**
     * 踢人
     *
     * @param seat
     */
    private void kickSeat(@NonNull VoiceRoomSeat seat) {
        VoiceRoomUser user = seat.getUser();
        String nick = user != null ? user.getNick() : "";
        final String text = "已将“" + nick + "”踢下麦位";
        anchor.kickSeat(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastHelper.showToast(text);
            }
        });
    }

    /**
     * 关闭麦位
     *
     * @param seat
     */
    private void closeSeat(VoiceRoomSeat seat) {
        final String text = "\"麦位" + (seat.getIndex() + 1) + "\"已关闭";
        anchor.closeSeat(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastHelper.showToast(text);
            }
        });
    }

    private int inviteIndex = -1;

    /**
     * 抱麦
     *
     * @param seat
     */
    private void inviteSeat0(VoiceRoomSeat seat) {
        inviteIndex = seat.getIndex();
        anchor.fetchSeats(new SuccessCallback<List<VoiceRoomSeat>>() {
            @Override
            public void onSuccess(List<VoiceRoomSeat> param) {
                new MemberSelectDialog(AnchorRoomActivity.this, getOnSeatAccounts(param), member -> {
                    //被抱用户
                    if (member != null) {
                        inviteSeat(member);
                    }
                }).show();
            }
        });
    }

    /**
     * 屏蔽麦位
     *
     * @param seat
     */
    private void muteSeat(VoiceRoomSeat seat) {
        final String text = "该麦位语音已被屏蔽，无法发言";
        anchor.muteSeat(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastHelper.showToast(text);
            }
        });
    }

    /**
     * 打开麦位
     *
     * @param seat
     */
    public void openSeat(@NonNull VoiceRoomSeat seat) {
        String msg = "";
        String msgError = "";
        switch (seat.getStatus()) {
            case Status.CLOSED:
                int position = seat.getIndex() + 1;
                msg = "“麦位" + position + "”已打开”";
                msgError = "“麦位" + position + "”打开失败”";
                break;
            case Status.FORBID:
            case Status.AUDIO_MUTED:
                msg = "“该麦位已“解除语音屏蔽”";
                msgError = "该麦位“解除语音屏蔽”失败";
                break;
            case Status.AUDIO_CLOSED_AND_MUTED:
                msg = "该麦位已“解除语音屏蔽”";
                msgError = "该麦位“解除语音屏蔽”失败";
                break;
        }
        String text = msg;
        String textError = msgError;

        anchor.openSeat(seat, new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast(text);
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.showToast(textError + " code " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.showToast(textError + " " + throwable.getMessage());
            }
        });
    }

    /**
     * 获取麦上用户
     *
     * @param seats
     * @return
     */
    private static List<String> getOnSeatAccounts(List<VoiceRoomSeat> seats) {
        List<String> accounts = new ArrayList<>();
        for (VoiceRoomSeat seat : seats) {
            if (seat.isOn()) {
                String account = seat.getAccount();
                if (!TextUtils.isEmpty(account)) {
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }


    /**
     * 抱麦
     *
     * @param member
     */
    private void inviteSeat(@NonNull VoiceRoomUser member) {
        anchor.getRoomQuery().isMember(member.getAccount(), new SuccessCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean in) {
                if (!in) {
                    ToastHelper.showToast("操作失败:用户离开房间");
                    return;
                }
                anchor.fetchSeats(new SuccessCallback<List<VoiceRoomSeat>>() {
                    @Override
                    public void onSuccess(List<VoiceRoomSeat> seats) {
                        inviteSeat(member, inviteIndex, seats);
                    }
                });
            }
        });
    }

    /**
     * 抱麦
     *
     * @param member
     * @param index
     * @param seats
     */
    private void inviteSeat(@NonNull VoiceRoomUser member, int index, @NonNull List<VoiceRoomSeat> seats) {
        String account = member.getAccount();
        List<VoiceRoomSeat> userSeats = VoiceRoomSeat.find(seats, account);

        for (VoiceRoomSeat seat : userSeats) {
            if (seat != null && seat.isOn()) {
                ToastHelper.showToast("操作失败:当前用户已在麦位上");
                return;
            }
        }

        int position = -1;//当前用户申请麦位位置
        VoiceRoomSeat seat = VoiceRoomSeat.findByStatus(userSeats, account, Status.APPLY);
        if (seat != null) {
            position = seat.getIndex();
        }

        //拒绝申请麦位上不是选中用户的观众
        VoiceRoomSeat local = anchor.getSeat(index);
        if (local.getStatus() == Status.APPLY && !local.isSameAccount(account)) {
            denySeatApply(local);
        }

        //拒绝选中用户的观众在别的麦位的申请
        if (position != -1 && position != index) {
            denySeatApply(anchor.getSeat(position));
        }
        inviteSeat(new VoiceRoomSeat(
                index,
                seats.get(index).getStatus() == Status.FORBID ? Status.FORBID : Status.ON, Reason.ANCHOR_INVITE, member
        ));
    }

    /**
     * 抱麦
     *
     * @param seat
     */
    private void inviteSeat(@NonNull VoiceRoomSeat seat) {
        VoiceRoomUser user = seat.getUser();
        String nick = user != null ? user.getNick() : "";
        final String text = "已将" + nick + "抱上麦位" + (seat.getIndex() + 1);

        boolean ret = anchor.inviteSeat(seat, new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastHelper.showToast(text);
            }
        });
        if (!ret) {
            denySeatApply(seat);
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
                        Runnable runnable = () -> finish();
                        runnable.run();
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        if (!Network.getInstance().isConnected()) {
                            ToastHelper.showToast("网络问题导致房间解散失败");
                        } else {
                            ToastHelper.showToast("房间解散失败 " + errorMsg);
                        }
                        Runnable runnable = () -> finish();
                        runnable.run();
                    }
                });
    }
}
