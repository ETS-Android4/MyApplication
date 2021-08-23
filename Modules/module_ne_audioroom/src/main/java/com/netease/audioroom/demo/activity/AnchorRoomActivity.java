package com.netease.audioroom.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.dialog.RoomSeatApplyListDialog;
import com.netease.audioroom.demo.dialog.RoomSeatMenuDialog;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat.Status;
import com.netease.audioroom.demo.voiceroom.interfaces.Anchor;
import com.netease.yunxin.android.lib.network.common.BaseResponse;

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

    private ImageView ivApplyList;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_room;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatRoomHelper.enterRoom(true);
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
        ivApplyList = findViewById(R.id.iv_room_apply);
        ivApplyList.setVisibility(View.INVISIBLE);
        ivApplyList.setClickable(true);
        ivApplyList.setOnClickListener(view -> {
                    //申请上麦弹窗
                    RoomSeatApplyListDialog dialog = new RoomSeatApplyListDialog();
                    dialog.show(getSupportFragmentManager(), dialog.getTag());
                }
        );
    }

    @Override
    protected void onSeatItemClick(VoiceRoomSeat seat, int position) {
        if (seat.getStatus() == Status.APPLY) {
            ToastUtils.showShort(getString(R.string.applying_now));
        } else {
            RoomSeatMenuDialog dialog = new RoomSeatMenuDialog(this, seat, createMenuItem(seat));
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
            ivApplyList.setVisibility(View.VISIBLE);
        } else {
            ivApplyList.setVisibility(View.INVISIBLE);
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
                        ToastUtils.showShort("退出房间成功");
                        finish();
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        ToastUtils.showShort("房间解散失败 " + errorMsg);
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
