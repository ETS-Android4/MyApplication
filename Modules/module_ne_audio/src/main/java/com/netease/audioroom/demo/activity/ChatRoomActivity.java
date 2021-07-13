package com.netease.audioroom.demo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.netease.audioroom.demo.ChatRoomManager;
import com.netease.audioroom.demo.R;

public class ChatRoomActivity extends AppCompatActivity {

    //public ActivityAnchorBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor);

        //mBinding = ActivityAnchorBinding.inflate(getLayoutInflater());
        //setContentView(mBinding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //ChatRoomManager.getInstance().onActivityAttach(new ChatRoomCallback(mBinding));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatRoomManager.getInstance().onActivityDetach();
    }

}