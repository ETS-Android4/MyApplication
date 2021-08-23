package com.netease.audioroom.demo.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.netease.audioroom.demo.ChatRoomHelper;
import com.netease.audioroom.demo.act.ChatRoomManager;
import com.netease.audioroom.demo.adapter.BaseRecycleAdapter;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.callback.SuccessCallback;

import java.util.List;

/**
 * 麦位菜单
 */
public class RoomSeatMenuDialog extends BaseRecyclerDialogFragment<String> {

    private final FragmentActivity mActivity;

    private final VoiceRoomSeat mSeat;
    private final List<String> mMenus;

    @Override
    protected BaseQuickAdapter<String, BaseViewHolder> setAdapter() {
        return new BaseRecycleAdapter<>();
    }

    @Override
    protected boolean canRefresh() {
        return false;
    }

    public RoomSeatMenuDialog(FragmentActivity activity, VoiceRoomSeat seat, List<String> menus) {
        this.mActivity = activity;
        this.mSeat = seat;
        this.mMenus = menus;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.setNewInstance(mMenus);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        super.onItemClick(adapter, view, position);
        onSeatAction(mSeat, adapter.getData().get(position).toString());
        dismiss();
    }

    @Override
    protected void setAttributes(@NonNull WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.height = SizeUtils.dp2px(300);
        params.gravity = Gravity.BOTTOM;
    }

    private void onSeatAction(VoiceRoomSeat seat, @NonNull String item) {
        switch (item) {
            case "上麦":
                ToastUtils.showShort("上麦");
                break;
            case "申请上麦":
                //ChatRoomHelper.applySeat(seat, new SuccessCallback<Void>() {
                //    @Override
                //    public void onSuccess(Void param) {
                //        ToastUtils.showShort("已申请上麦");
                //    }
                //});
                ChatRoomManager.getInstance().toggleApplySeat(seat, new SuccessCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        ToastUtils.showShort("已申请上麦");
                    }
                });
                break;
            case "下麦":
                //ChatRoomHelper.leaveSeat();
                ChatRoomManager.getInstance().toggleLeaveSeat();
                break;
            case "将成员抱上麦位":
                RoomMemberListDialog dialog = new RoomMemberListDialog(seat);
                dialog.show(mActivity.getSupportFragmentManager(), dialog.getTag());
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
        }
    }
}
