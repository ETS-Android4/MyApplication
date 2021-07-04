package com.netease.audioroom.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;

import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.dialog.ListItemDialog;
import com.netease.audioroom.demo.dialog.NotificationDialog;
import com.netease.audioroom.demo.dialog.TopTipsDialog;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Reason;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Status;
import com.netease.yunxin.nertc.model.interfaces.Audience;
import com.netease.yunxin.nertc.model.interfaces.AudiencePlay;
import com.netease.yunxin.nertc.util.SuccessCallback;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * 观众页
 */
public class AudienceRoomActivity extends BaseRoomActivity implements Audience.Callback {

    public static void start(Context context, VoiceRoomInfo model) {
        Intent intent = new Intent(context, AudienceRoomActivity.class);
        intent.putExtra(EXTRA_VOICE_ROOM_INFO, model);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        }
    }

    private TopTipsDialog mTopTipsDialog;
    private Audience audience;

    private ListItemDialog mCancelApplyDialog;

    private Disposable disposable;
    private final PublishSubject<VoiceRoomSeat> seatSource = PublishSubject.create();

    @Override
    protected int getContentViewID() {
        return R.layout.activity_audience;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatRoomHelper.enterRoom(false);

        watchNetWork();

        disposable = seatSource.serialize().throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(seat -> {
                    switch (seat.getStatus()) {
                        case Status.INIT:
                        case Status.FORBID:
                            if (checkSeat()) {
                                ChatRoomHelper.applySeat(seat);
                            }
                            break;
                        case Status.APPLY:
                            ToastHelper.showToast("该麦位正在被申请,\n请尝试申请其他麦位");
                            break;
                        case Status.ON:
                        case Status.AUDIO_MUTED:
                        case Status.AUDIO_CLOSED:
                        case Status.AUDIO_CLOSED_AND_MUTED:
                            if (seat.isSameAccount(DemoCache.getAccountId())) {
                                promptLeaveSeat();
                            } else {
                                ToastHelper.showToast("当前麦位有人");
                            }
                            break;
                        case Status.CLOSED:
                            ToastHelper.showToast("该麦位已被关闭");
                            break;
                    }
                }, Throwable::printStackTrace);
    }

    private void watchNetWork() {
        NetworkChange.getInstance().getNetworkLiveData().observeInitAware(this, network -> {
            if (network != null && network.isConnected()) {
                if (mTopTipsDialog != null) {
                    mTopTipsDialog.dismiss();
                }
                loadSuccess();
                if (audience != null) {
                    audience.restartAudioOrNot();
                    audience.refreshSeat();
                }
            } else {
                Bundle bundle = new Bundle();
                mTopTipsDialog = new TopTipsDialog();
                TopTipsDialog.Style style = new TopTipsDialog.Style("网络断开", 0, R.drawable.neterrricon, 0);
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
        ChatRoomHelper.initAudience(this);
    }

    @Override
    protected void setupView() {
        mTopTipsDialog = new TopTipsDialog();
        mic.setVisibility(View.GONE);
    }

    @Override
    protected synchronized void onSeatItemClick(VoiceRoomSeat seat, int position) {
        seatSource.onNext(seat);
    }

    @Override
    protected void closeRoom() {
        if (!mVoiceRoomInfo.isSupportCDN()) {
            finish();
        }
        VoiceRoomSeat seat = audience.getSeat();
        boolean isInChannel = seat != null && seat.isOn();
        if (!isInChannel) {
            finish();
        }
    }

    //
    // Audience.Callback
    //

    private boolean canShowTip = false;

    /**
     * 上麦请求被拒绝
     *
     * @param otherOn 是否被他人占用
     */
    @Override
    public void onSeatApplyDenied(boolean otherOn) {
        if (otherOn) {
            ToastHelper.showToast("申请麦位已被拒绝");
            if (mTopTipsDialog != null) {
                mTopTipsDialog.dismiss();
            }
        } else {
            new NotificationDialog(this)
                    .setTitle("通知")
                    .setContent("您的申请已被拒绝")
                    .setPositive("知道了", v -> {
                        canShowTip = false;
                        if (mCancelApplyDialog != null && mCancelApplyDialog.isShowing()) {
                            mCancelApplyDialog.dismiss();
                        }
                        if (mTopTipsDialog != null) {
                            mTopTipsDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    /**
     * 上麦
     *
     * @param seat
     * @param last 是否为恢复时
     */
    @Override
    public void onEnterSeat(VoiceRoomSeat seat, boolean last) {
        mic.setVisibility(View.VISIBLE);
        if (!last) {
            hintSeatState(seat, true);
        }
    }

    /**
     * 下麦
     *
     * @param seat
     * @param bySelf 是否为自己下麦
     */
    @Override
    public void onLeaveSeat(VoiceRoomSeat seat, boolean bySelf) {
        mic.setVisibility(View.GONE);
        if (!bySelf) {
            hintSeatState(seat, false);
        }
    }

    /**
     * 麦位静音
     */
    @Override
    public void onSeatMuted() {
        if (mTopTipsDialog != null) {
            mTopTipsDialog.dismiss();
        }
        new NotificationDialog(this)
                .setTitle("通知")
                .setContent("该麦位被主播“屏蔽语音”\n 现在您已无法进行语音互动")
                .setPositive("知道了", null)
                .show();
    }

    /**
     * 麦位关闭
     */
    @Override
    public void onSeatClosed() {
        if (mTopTipsDialog != null) {
            mTopTipsDialog.dismiss();
        }
    }

    /**
     * 是否被禁言（发送文字消息）
     *
     * @param muted
     */
    @Override
    public void onTextMuted(boolean muted) {
        tvInput.setEnabled(!muted);
        if (muted) {
            tvInput.setHint("您已被禁言");
            tvInput.setFocusable(false);
            ToastHelper.showToast("您已被禁言");
        } else {
            tvInput.setHint("一起聊聊吧~");
            tvInput.requestFocus();
            ToastHelper.showToast("您的禁言被解除");
        }
    }


    /**
     * 麦位状态提示
     *
     * @param seat
     * @param on
     */
    public void hintSeatState(VoiceRoomSeat seat, boolean on) {
        if (on) {
            Bundle bundle = new Bundle();
            switch (seat.getReason()) {
                case Reason.ANCHOR_INVITE: {
                    int position = seat.getIndex() + 1;
                    new NotificationDialog(AudienceRoomActivity.this)
                            .setTitle("通知")
                            .setContent("您已被主播抱上“麦位”" + position + "\n" +
                                    "现在可以进行语音互动啦\n" +
                                    "如需下麦，可点击自己的头像或下麦按钮")
                            .setPositive("知道了", v -> {
                                canShowTip = false;
                                if (mCancelApplyDialog != null && mCancelApplyDialog.isShowing()) {
                                    mCancelApplyDialog.dismiss();
                                }
                                if (mTopTipsDialog != null) {
                                    mTopTipsDialog.dismiss();
                                }
                            })
                            .show();
                    break;
                }
                //主播同意上麦
                case Reason.ANCHOR_APPROVE_APPLY: {
                    canShowTip = false;
                    if (mCancelApplyDialog != null && mCancelApplyDialog.isShowing()) {
                        mCancelApplyDialog.dismiss();
                    }
                    if (mTopTipsDialog != null) {
                        mTopTipsDialog.dismiss();
                    }

                    TopTipsDialog.Style style = new TopTipsDialog.Style("申请通过!",
                            R.color.color_000000,
                            R.drawable.right,
                            R.color.color_000000);
                    bundle.putParcelable(mTopTipsDialog.TAG, style);
                    mTopTipsDialog.setArguments(bundle);
                    mTopTipsDialog.show(getSupportFragmentManager(), mTopTipsDialog.TAG);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mTopTipsDialog.dismiss();
                        }
                    }, 2000); // 延时2秒
                    break;
                }
                case Reason.CANCEL_MUTED: {
                    new NotificationDialog(this)
                            .setTitle("通知")
                            .setContent("该麦位被主播“解除语音屏蔽”\n" +
                                    "现在您可以再次进行语音互动了")
                            .setPositive("知道了", null)
                            .show();
                    break;
                }
                default: {
                }
            }
            if (mTopTipsDialog != null) {
                mTopTipsDialog.dismiss();
            }
        } else {
            if (mTopTipsDialog != null) {
                mTopTipsDialog.dismiss();
            }
            if (seat.getReason() == Reason.ANCHOR_KICK) {
                new NotificationDialog(this)
                        .setTitle("通知")
                        .setContent("您已被主播请下麦位")
                        .setPositive("知道了", null)
                        .show();
            }
        }
    }

    //
    // ==========
    //


    /**
     * 是否可以上麦
     *
     * @return
     */
    private boolean checkSeat() {
        VoiceRoomSeat seat = audience.getSeat();
        if (seat != null) {
            if (seat.getStatus() == Status.CLOSED) {
                ToastHelper.showToast("麦位已关闭");
            } else if (seat.isOn()) {
                ToastHelper.showToast("您已在麦上");
            } else {
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * 上麦成功
     */
    private void onApplySeatSuccess() {
        Bundle bundle = new Bundle();
        TopTipsDialog.Style style = new TopTipsDialog.Style("已申请上麦，等待通过...  <font color=\"#0888ff\">取消</color>", 0, 0,
                0);
        bundle.putParcelable(mTopTipsDialog.TAG, style);
        mTopTipsDialog.setArguments(bundle);
        mTopTipsDialog.show(getSupportFragmentManager(), mTopTipsDialog.TAG);
        canShowTip = true;
        mTopTipsDialog.setClickListener(() -> {
            mTopTipsDialog.dismiss();
            if (mCancelApplyDialog != null && mCancelApplyDialog.isShowing()) {
                mCancelApplyDialog.dismiss();
            }
            mCancelApplyDialog = new ListItemDialog(AudienceRoomActivity.this).setOnItemClickListener(item -> {
                if ("确认取消申请上麦".equals(item)) {
                    ChatRoomHelper.cancelSeatApply();
                    canShowTip = false;
                }
            });
            mCancelApplyDialog.setOnDismissListener(dialog1 -> {
                if (canShowTip) {
                    mTopTipsDialog.show(getSupportFragmentManager(), mTopTipsDialog.TAG);
                }
            });
            mCancelApplyDialog.show(Arrays.asList("确认取消申请上麦", "取消"));
        });
    }

    /**
     * 促使下麦？
     */
    private void promptLeaveSeat() {
        if (audience.getSeat() == null) {
            return;
        }
        new ListItemDialog(AudienceRoomActivity.this)
                .setOnItemClickListener(item -> {
                    if ("下麦".equals(item)) {
                        ChatRoomHelper.leaveSeat();
                    }
                }).show(Arrays.asList("下麦", "取消"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
