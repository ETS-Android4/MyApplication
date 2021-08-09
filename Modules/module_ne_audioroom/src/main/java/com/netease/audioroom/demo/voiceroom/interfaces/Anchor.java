package com.netease.audioroom.demo.voiceroom.interfaces;

import com.netease.audioroom.demo.voiceroom.RoomQuery;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.nimlib.sdk.RequestCallback;

import java.util.List;

/**
 * 主播操作接口
 */
public interface Anchor {

    /**
     * 申请上麦
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     */
    void applySeat(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 通过上麦请求
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     * @return 动作是否执行
     */
    boolean approveSeatApply(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 拒绝上麦请求
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     */
    void denySeatApply(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 跳麦
     *
     * @param mySeat   {@link VoiceRoomSeat 从 fromSeat 麦位}
     * @param toSeat   {@link VoiceRoomSeat 跳到 toSeat 麦位}
     * @param callback {@link RequestCallback 回调}在
     */
    void jumpSeatApply(VoiceRoomSeat mySeat, VoiceRoomSeat toSeat, RequestCallback<Void> callback);

    /**
     * 下麦
     *
     * @param callback {@link RequestCallback 回调}
     */
    void leaveSeat(RequestCallback<Void> callback);

    /**
     * 打开麦位
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     */
    void openSeat(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 关闭麦位
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     */
    void closeSeat(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 静音麦位
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     */
    void muteSeat(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 抱上麦位
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     * @return 动作是否执行
     */
    boolean inviteSeat(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 踢下麦位
     *
     * @param seat     {@link VoiceRoomSeat 麦位}
     * @param callback {@link RequestCallback 回调}
     */
    void kickSeat(VoiceRoomSeat seat, RequestCallback<Void> callback);

    /**
     * 获取服务器最新成员列表
     *
     * @param callback {@link RequestCallback 回调}
     */
    void fetchSeats(RequestCallback<List<VoiceRoomSeat>> callback);

    /**
     * 获取本地麦位
     *
     * @param index 位置索引
     * @return 麦位
     */
    VoiceRoomSeat getSeat(int index);

    /**
     * 获取当前上麦请求列表
     *
     * @return 麦位列表
     */
    List<VoiceRoomSeat> getApplySeats();

    /**
     * 获取房间查询接口
     *
     * @return 房间查询
     */
    RoomQuery getRoomQuery();

    void notifyStreamRestarted();

    /**
     * 主播回调接口
     */
    interface Callback {
        /**
         * 上麦请求列表
         *
         * @param seats {@link VoiceRoomSeat 麦位列表}
         */
        void onApplySeats(List<VoiceRoomSeat> seats);
    }

    /**
     * 设置主播回调
     *
     * @param callback {@link Anchor.Callback 回调}
     */
    void setCallback(Callback callback);
}