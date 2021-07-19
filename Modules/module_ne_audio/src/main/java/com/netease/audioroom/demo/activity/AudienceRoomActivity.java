package com.netease.audioroom.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.dialog.TopTipsDialogFragment;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Reason;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat.Status;
import com.netease.yunxin.nertc.model.interfaces.Audience;
import com.netease.yunxin.nertc.util.SuccessCallback;

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

    private TopTipsDialogFragment mTopTipsDialogFragment;

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
                            ChatRoomHelper.applySeat(seat, new SuccessCallback<Void>() {
                                @Override
                                public void onSuccess(Void param) {
                                    //展示申请上麦提示弹窗
                                    Bundle bundle = new Bundle();
                                    TopTipsDialogFragment.Style style = new TopTipsDialogFragment.Style("已申请上麦，等待通过...  <font color=\"#0888ff\">取消</color>", 0, 0, 0);
                                    bundle.putParcelable(mTopTipsDialogFragment.getTag(), style);
                                    mTopTipsDialogFragment.setArguments(bundle);
                                    mTopTipsDialogFragment.show(getSupportFragmentManager(), mTopTipsDialogFragment.getTag());
                                    mTopTipsDialogFragment.setClickListener(() -> {
                                        //取消上麦
                                        ChatRoomHelper.cancelSeatApply();
                                        mTopTipsDialogFragment.dismiss();
                                    });
                                }
                            });
                            break;
                        case Status.APPLY:
                            ToastUtils.showShort("该麦位正在被申请,\n请尝试申请其他麦位");
                            break;
                        case Status.ON:
                        case Status.AUDIO_MUTED:
                        case Status.AUDIO_CLOSED:
                        case Status.AUDIO_CLOSED_AND_MUTED:
                            //是否是自己
                            if (seat.isSameAccount(DemoCache.getAccountId())) {
                                ChatRoomHelper.leaveSeat();
                            } else {
                                ToastUtils.showShort("当前麦位有人");
                            }
                            break;
                        case Status.CLOSED:
                            ToastUtils.showShort("该麦位已被关闭");
                            break;
                    }
                }, Throwable::printStackTrace);
    }

    private void watchNetWork() {
        NetworkChange.getInstance().getNetworkLiveData().observeInitAware(this, network -> {
            if (network != null && network.isConnected()) {
                if (mTopTipsDialogFragment != null) {
                    mTopTipsDialogFragment.dismiss();
                }
                loadSuccess();
                //刷新音频和座位信息
                ChatRoomHelper.restartAudioAndSeat();
            } else {
                Bundle bundle = new Bundle();
                mTopTipsDialogFragment = new TopTipsDialogFragment();
                TopTipsDialogFragment.Style style = new TopTipsDialogFragment.Style("网络断开", 0, R.drawable.neterrricon, 0);
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
        ChatRoomHelper.initAudience(this);
    }

    @Override
    protected void setupView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mVoiceRoomInfo.isSupportCDN()) {
                    ChatRoomHelper.leaveRoom();
                }
                if (ChatRoomHelper.isInChannel()) {
                    ChatRoomHelper.leaveRoom();
                } else {
                    finish();
                }
            }
        });
        mTopTipsDialogFragment = new TopTipsDialogFragment();
        mic.setVisibility(View.GONE);
    }

    @Override
    protected synchronized void onSeatItemClick(VoiceRoomSeat seat, int position) {
        seatSource.onNext(seat);
    }

    //
    // Audience.Callback
    //

    /**
     * 上麦请求被拒绝
     *
     * @param otherOn 是否被他人占用
     */
    @Override
    public void onSeatApplyDenied(boolean otherOn) {
        if (otherOn) {
            ToastUtils.showShort("申请麦位已被拒绝");
            if (mTopTipsDialogFragment != null) {
                mTopTipsDialogFragment.dismiss();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("通知")
                    .setMessage("您的申请已被拒绝")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mTopTipsDialogFragment != null) {
                                mTopTipsDialogFragment.dismiss();
                            }
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * 上麦
     *
     * @param last 是否为恢复时
     */
    @Override
    public void onEnterSeat(VoiceRoomSeat seat, boolean last) {
        mic.setVisibility(View.VISIBLE);
        if (!last) {
            if (seat.getReason() == Reason.ANCHOR_APPROVE_APPLY) {//主播同意上麦
                if (mTopTipsDialogFragment != null) {
                    mTopTipsDialogFragment.dismiss();
                }
                new AlertDialog.Builder(this)
                        .setTitle("通知")
                        .setMessage("申请通过")
                        .setPositiveButton("知道了", null)
                        .create()
                        .show();
            } else if (seat.getReason() == Reason.ANCHOR_INVITE) {//主播抱上麦
                int position = seat.getIndex() + 1;
                new AlertDialog.Builder(this)
                        .setTitle("通知")
                        .setMessage("您已被主播抱上“麦位”" + position + "\n" + "现在可以进行语音互动啦\n" + "如需下麦，可点击自己的头像或下麦按钮")
                        .setPositiveButton("知道了", null)
                        .create()
                        .show();
            }
        }
    }

    /**
     * 下麦
     */
    @Override
    public void onLeaveSeat(VoiceRoomSeat seat, boolean bySelf) {
        mic.setVisibility(View.GONE);
        if (!bySelf) {
            if (seat.getReason() == Reason.ANCHOR_KICK) {
                new AlertDialog.Builder(this)
                        .setTitle("通知")
                        .setMessage("您已被主播请下麦位")
                        .setPositiveButton("知道了", null)
                        .create()
                        .show();
            }
        }
    }

    /**
     * 麦位静音
     */
    @Override
    public void onSeatMuted() {
        if (mTopTipsDialogFragment != null) {
            mTopTipsDialogFragment.dismiss();
        }
        new AlertDialog.Builder(this)
                .setTitle("通知")
                .setMessage("该麦位被主播“屏蔽语音”\n 现在您已无法进行语音互动")
                .setPositiveButton("知道了", null)
                .create()
                .show();
    }

    /**
     * 麦位关闭
     */
    @Override
    public void onSeatClosed() {
        if (mTopTipsDialogFragment != null) {
            mTopTipsDialogFragment.dismiss();
        }
    }

    /**
     * 是否被禁言（发送文字消息）
     */
    @Override
    public void onTextMuted(boolean muted) {
        tvInput.setEnabled(!muted);
        if (muted) {
            tvInput.setHint("您已被禁言");
            tvInput.setFocusable(false);
            ToastUtils.showShort("您已被禁言");
        } else {
            tvInput.setHint("一起聊聊吧~");
            tvInput.requestFocus();
            ToastUtils.showShort("您的禁言被解除");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
