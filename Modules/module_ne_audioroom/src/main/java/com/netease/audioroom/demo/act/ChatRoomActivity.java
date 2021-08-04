package com.netease.audioroom.demo.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.databinding.ActivityAnchorBinding;
import com.netease.audioroom.demo.voiceroom.bean.VoiceRoomInfo;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String EXTRA_VOICE_ROOM_INFO = "extra_voice_room_info";
    public static final String EXTRA_VOICE_ANCHOR_MODE = "extra_voice_anchor_mode";

    public static void start(Context context, VoiceRoomInfo info, boolean anchorMode) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra(EXTRA_VOICE_ROOM_INFO, info);
        intent.putExtra(EXTRA_VOICE_ANCHOR_MODE, anchorMode);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        }
    }

    private boolean isAnchorMode;
    private VoiceRoomInfo mVoiceRoomInfo;

    public ActivityAnchorBinding mBinding;
    public ChatRoomCallback mChatRoomCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_anchor);

        mBinding = ActivityAnchorBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        isAnchorMode = getIntent().getBooleanExtra(EXTRA_VOICE_ANCHOR_MODE, false);
        mVoiceRoomInfo = (VoiceRoomInfo) getIntent().getSerializableExtra(EXTRA_VOICE_ROOM_INFO);

        Log.e("TAG", "VoiceRoomInfo " + new Gson().toJson(mVoiceRoomInfo));

        mChatRoomCallback = new ChatRoomCallback(this, mBinding, isAnchorMode, mVoiceRoomInfo);

        //聊天室初始化
        ChatRoomManager.getInstance().initRoom(this, mVoiceRoomInfo);
        //进入直播间
        ChatRoomManager.getInstance().enterRoom(isAnchorMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ChatRoomManager.getInstance().onActivityAttach(mChatRoomCallback);
        //ChatRoomManager.getInstance().initRoom(this, mVoiceRoomInfo, mChatRoomCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatRoomManager.getInstance().onActivityDetach();
    }
}