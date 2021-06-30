package com.netease.audioroom.demo.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.LoginManager;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.FunctionAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.http.ChatRoomNetConstants;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.nimlib.sdk.StatusCode;

import java.util.Arrays;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否是任务栈中的根Activity，如果是就不做任何处理，如果不是则直接finish掉;
        if (!isTaskRoot()) {
            finish();
            return;
        }
        initViews();
        showLoading();
        watchNetWork();
    }

    protected void initViews() {
        // 功能列表初始化
        RecyclerView rvFunctionList = findViewById(R.id.rv_function_list);
        rvFunctionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvFunctionList.setAdapter(new FunctionAdapter(this, Arrays.asList(
                new FunctionAdapter.FunctionItem(FunctionAdapter.TYPE_VIEW_TITLE, getString(R.string.funciton_title)),
                // 每个业务功能入口均在此处生成 item

                // 语音
                new FunctionAdapter.FunctionItem(R.drawable.icon_function_chat_room, getString(R.string.voice_chat),
                        getString(R.string.function_desc), () -> {
                    RoomListActivity.start(MainActivity.this, ChatRoomNetConstants.ROOM_TYPE_CHAT);
                })
                // KTV
                , new FunctionAdapter.FunctionItem(R.drawable.icon_funcation_ktv, getString(R.string.ktv),
                        getString(R.string.function_desc), () -> {
                    RoomListActivity.start(MainActivity.this, ChatRoomNetConstants.ROOM_TYPE_KTV);
                }))));

        View toCreate = findViewById(R.id.iv_new_live);
        toCreate.setOnClickListener(v -> {
            CreateRoomActivity.start(MainActivity.this, ChatRoomNetConstants.ROOM_TYPE_CHAT);
        });
    }

    private void watchNetWork() {
        NetworkChange.getInstance().getmNetworkLiveData().observe(this, network -> {
            if (network != null && network.isConnected()) {
                onNetwork();
            } else {
                showNetError();
            }
        });
    }

    // 不需要每次登录。网络更改时im自动登录
    // not need every time login. im auto login when network change
    private void onNetwork() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.tryLogin();
        loginManager.setCallback(new LoginManager.Callback() {

            @Override
            public void onSuccess(AccountInfo accountInfo) {
                loadSuccess();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                showError();
            }
        });
    }

    @Override
    protected void onLoginEvent(StatusCode statusCode) {
    }

}
