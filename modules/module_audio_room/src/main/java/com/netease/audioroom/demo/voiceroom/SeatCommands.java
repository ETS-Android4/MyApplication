package com.netease.audioroom.demo.voiceroom;

import android.text.TextUtils;

import com.netease.audioroom.demo.util.JsonUtil;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat.Reason;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomSeat.Status;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomUser;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

import org.json.JSONException;
import org.json.JSONObject;

public class SeatCommands {

    /**
     * 上麦
     */
    public static CustomNotification applySeat(VoiceRoomInfo voiceRoomInfo, VoiceRoomUser user, VoiceRoomSeat seat) {
        String content;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(SeatCommandDef.COMMAND, SeatCommandDef.APPLY_SEAT);
            jsonObject.put(SeatCommandDef.INDEX, seat.getIndex());
            jsonObject.put(SeatCommandDef.NICK, user.getNick());
            jsonObject.put(SeatCommandDef.AVATAR, user.getAvatar());
            content = jsonObject.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }

        CustomNotification notification = new CustomNotification();
        notification.setSessionId(voiceRoomInfo.getCreatorAccount());
        notification.setSessionType(SessionTypeEnum.P2P);
        notification.setFromAccount(user.getAccount());
        notification.setContent(content);
        return notification;
    }

    /**
     * 下麦
     */
    public static CustomNotification leaveSeat(VoiceRoomInfo voiceRoomInfo, VoiceRoomUser user, VoiceRoomSeat seat) {
        String content;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(SeatCommandDef.COMMAND, SeatCommandDef.LEAVE_SEAT);
            jsonObject.put(SeatCommandDef.INDEX, seat.getIndex());
            content = jsonObject.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        CustomNotification notification = new CustomNotification();
        notification.setSessionId(voiceRoomInfo.getCreatorAccount());
        notification.setSessionType(SessionTypeEnum.P2P);
        notification.setFromAccount(user.getAccount());
        notification.setContent(content);
        return notification;
    }

    /**
     * 取消申请上麦
     */
    public static CustomNotification cancelSeatApply(VoiceRoomInfo voiceRoomInfo, VoiceRoomUser user, VoiceRoomSeat seat) {
        String content;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(SeatCommandDef.COMMAND, SeatCommandDef.CANCEL_SEAT_APPLY);
            jsonObject.put(SeatCommandDef.INDEX, seat.getIndex());
            content = jsonObject.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        CustomNotification notification = new CustomNotification();
        notification.setSessionId(voiceRoomInfo.getCreatorAccount());
        notification.setSessionType(SessionTypeEnum.P2P);
        notification.setFromAccount(user.getAccount());
        notification.setContent(content);
        return notification;
    }

    public static int commandFrom(CustomNotification notification) {
        String content = notification.getContent();
        if (TextUtils.isEmpty(content)) {
            return 0;
        }
        JSONObject jsonObject = JsonUtil.parse(content);
        if (jsonObject == null) {
            return 0;
        }
        return jsonObject.optInt(SeatCommandDef.COMMAND, 0);
    }

    public static VoiceRoomSeat seatFrom(CustomNotification notification) {
        String content = notification.getContent();
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        JSONObject jsonObject = JsonUtil.parse(content);
        if (jsonObject == null) {
            return null;
        }
        int command = jsonObject.optInt(SeatCommandDef.COMMAND, 0);
        int index = jsonObject.optInt(SeatCommandDef.INDEX, -1);
        if (index < 0) {
            return null;
        }
        switch (command) {
            case SeatCommandDef.APPLY_SEAT: {
                String nick = jsonObject.optString(SeatCommandDef.NICK);
                String avatar = jsonObject.optString(SeatCommandDef.AVATAR);
                VoiceRoomUser user = new VoiceRoomUser(notification.getFromAccount(), nick, avatar);
                return new VoiceRoomSeat(index, Status.APPLY, Reason.INIT, user);
            }
            case SeatCommandDef.CANCEL_SEAT_APPLY:
            case SeatCommandDef.LEAVE_SEAT: {
                return new VoiceRoomSeat(index, Status.INIT, Reason.INIT, null);
            }
        }
        return null;
    }
}
