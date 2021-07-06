package com.netease.audioroom.demo.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.activity.AnchorRoomActivity;
import com.netease.audioroom.demo.adapter.MemberSelectListAdapter;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.yunxin.nertc.model.NERtcVoiceRoom;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;
import com.netease.yunxin.nertc.model.bean.VoiceRoomUser;
import com.netease.yunxin.nertc.model.interfaces.Anchor;

import java.util.ArrayList;
import java.util.List;

/**
 * 成员列表
 */
public class MemberSelectBottomDialog extends BaseBottomDialog {

    private OnMemberChosenListener listener;
    private MemberSelectListAdapter adapter;

    private Anchor anchor;

    private List<String> excludeAccounts = new ArrayList<>();

    public MemberSelectBottomDialog(@NonNull Activity activity, OnMemberChosenListener listener) {
        this(activity, null, listener);
    }


    public MemberSelectBottomDialog(@NonNull Activity activity, List<String> accounts, OnMemberChosenListener listener) {
        super(activity);
        this.listener = listener;
        if (accounts != null && !accounts.isEmpty()) {
            this.excludeAccounts.addAll(accounts);
        }
        this.anchor = NERtcVoiceRoom.sharedInstance(activity).getAnchor();
    }

    public MemberSelectBottomDialog(AnchorRoomActivity activity, List<VoiceRoomSeat> seats, OnMemberChosenListener listener) {
        super(activity);
        this.listener = listener;
        List<String> accounts = new ArrayList<>();
        for (VoiceRoomSeat seat : seats) {
            if (seat.isOn()) {
                String account = seat.getAccount();
                if (!TextUtils.isEmpty(account)) {
                    accounts.add(account);
                }
            }
        }
        if (!accounts.isEmpty()) {
            this.excludeAccounts.addAll(accounts);
        }
        this.anchor = NERtcVoiceRoom.sharedInstance(activity).getAnchor();
    }

    @Override
    protected void renderTopView(FrameLayout parent) {
        TextView titleView = new TextView(getContext());
        titleView.setText("选择成员");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(Color.parseColor("#ff333333"));
        FrameLayout.LayoutParams titleLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parent.addView(titleView, titleLayoutParams);

        ImageView cancelView = new ImageView(getContext());
        cancelView.setImageResource(R.drawable.icon_room_memeber_back_arrow);
        cancelView.setPadding(SizeUtils.dp2px(20), 0, 0, 0);
        FrameLayout.LayoutParams cancelLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parent.addView(cancelView, cancelLayoutParams);

        cancelView.setOnClickListener(v -> dismiss());
    }

    @Override
    protected void renderBottomView(FrameLayout parent) {
        RecyclerView rvMemberList = new RecyclerView(getContext());
        rvMemberList.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        int height = (int) (ScreenUtils.getScreenHeight() * 0.8) - SizeUtils.dp2px(48);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        rvMemberList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MemberSelectListAdapter(getContext());
        rvMemberList.setAdapter(adapter);
        parent.addView(rvMemberList, layoutParams);

        fetchRoomMembers(excludeAccounts);

    }

    private void fetchRoomMembers(List<String> excludeAccounts) {
        RequestCallback<List<VoiceRoomUser>> callback = new RequestCallback<List<VoiceRoomUser>>() {
            @Override
            public void onSuccess(List<VoiceRoomUser> members) {
                adapter.updateDataSource(members);
                adapter.setOnItemClickListener(item -> {
                    VoiceRoomUser member = members.get(item);
                    if (listener != null) {
                        listener.onMemberChosen(member);
                    }
                    dismiss();
                });
            }

            @Override
            public void onFailed(int i) {
                ToastUtils.showShort("获取用户失败code" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                ToastUtils.showShort("获取用户失败Exception" + throwable.getMessage());
            }
        };
        if (!excludeAccounts.isEmpty()) {
            anchor.getRoomQuery().fetchMembersByAccount(excludeAccounts, false, callback);
        } else {
            anchor.getRoomQuery().fetchMembersByMuted(false, callback);
        }
    }

    public interface OnMemberChosenListener {
        void onMemberChosen(VoiceRoomUser member);
    }
}
