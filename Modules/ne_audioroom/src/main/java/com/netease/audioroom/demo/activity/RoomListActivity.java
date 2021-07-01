package com.netease.audioroom.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.blankj.utilcode.util.FragmentUtils;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.constant.Extras;
import com.netease.audioroom.demo.http.ChatRoomNetConstants;

public class RoomListActivity extends BaseActivity {

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, RoomListActivity.class);
        intent.putExtra(Extras.ROOM_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_room_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    protected void initViews() {
        View ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v ->
                finish()
        );
        Fragment fragment = RoomListFragment.newInstance(ChatRoomNetConstants.ROOM_TYPE_CHAT);
        FragmentUtils.add(getSupportFragmentManager(), fragment, R.id.fragment);

        View toCreate = findViewById(R.id.iv_new_live);
        toCreate.setOnClickListener(v ->
                CreateRoomActivity.start(RoomListActivity.this, ChatRoomNetConstants.ROOM_TYPE_CHAT)
        );
        loadSuccess();
    }
}