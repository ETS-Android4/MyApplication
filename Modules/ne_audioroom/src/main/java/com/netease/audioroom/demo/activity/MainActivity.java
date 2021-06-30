package com.netease.audioroom.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.ChatRoomListAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.base.LoginManager;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.http.ChatRoomNetConstants;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.util.NetworkChange;
import com.netease.audioroom.demo.util.NetworkUtils;
import com.netease.audioroom.demo.util.ToastHelper;
import com.netease.audioroom.demo.widget.loadsir.callback.FailureCallback;
import com.netease.yunxin.kit.alog.ALog;
import com.netease.yunxin.nertc.nertcvoiceroom.model.VoiceRoomInfo;

import java.util.ArrayList;

import static com.netease.yunxin.nertc.nertcvoiceroom.model.NERtcVoiceRoomDef.RoomAudioQuality.DEFAULT_QUALITY;

public class MainActivity extends BaseActivity {

    private View mEmptyView;

    private RecyclerView mRecyclerView;

    private ChatRoomListAdapter chatRoomListAdapter;

    private final ArrayList<VoiceRoomInfo> dataSource = new ArrayList<>();

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        initViews();
        fetchRoomList();
        watchNetWork();
    }

    private void initViews() {
        mEmptyView = findViewById(R.id.empty_view);

        chatRoomListAdapter = new ChatRoomListAdapter(this);
        chatRoomListAdapter.setItemClickListener((model, position) -> {
            //当前帐号创建的房间
            model.setAudioQuality(DEFAULT_QUALITY);

            if (TextUtils.equals(DemoCache.getAccountId(), model.getCreatorAccount())) {
                AnchorRoomActivity.start(this, model);//主播
            } else {
                AudienceRoomActivity.start(this, model);//观众
            }
        });

        mRecyclerView = findViewById(R.id.rv_room_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(chatRoomListAdapter);

        View toCreate = findViewById(R.id.iv_new_live);
        toCreate.setOnClickListener(v -> {
            getRandomName();
        });
    }

    private void watchNetWork() {
        NetworkChange.getInstance().getNetworkLiveData().observe(this, network -> {
            if (network != null && network.isConnected()) {
                onNetwork();
            } else {
                showError();
            }
        });
    }

    // not need every time login. im auto login when network change
    private void onNetwork() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.tryLogin();
        loginManager.setCallback(new LoginManager.Callback() {

            @Override
            public void onSuccess(AccountInfo accountInfo) {
                loadSuccess();
                requestLivePermission();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                loadShowCallback(FailureCallback.class);
            }
        });
    }

    private void fetchRoomList() {
        ChatRoomHttpClient client = ChatRoomHttpClient.getInstance();
        client.fetchChatRoomList(dataSource.size(), 20, ChatRoomNetConstants.ROOM_TYPE_CHAT,
                new ChatRoomHttpClient.ChatRoomHttpCallback<ArrayList<VoiceRoomInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<VoiceRoomInfo> voiceRoomInfos) {
                        dataSource.addAll(voiceRoomInfos);
                        chatRoomListAdapter.refreshList(dataSource);
                        showEmptyView();
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        if (dataSource.isEmpty()) {
                            showEmptyView();
                        }
                        ALog.e("FetchRoomList", "errorMsg is " + errorMsg + ", errorCode is " + code);
                    }
                });
    }

    private void showEmptyView() {
        if (dataSource.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 生成随机名
     */
    private void getRandomName() {
        ChatRoomHttpClient.getInstance().getRandomTopic(new ChatRoomHttpClient.ChatRoomHttpCallback<String>() {

            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    createRoom(s, ChatRoomNetConstants.PUSH_TYPE_CDN);
                }
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                // 获取随机名称失败；
            }
        });
    }

    /**
     * 创建直播间
     *
     * @param roomName
     * @param pushType ChatRoomNetConstants.PUSH_TYPE_RTC / ChatRoomNetConstants.PUSH_TYPE_CDN
     */
    private void createRoom(String roomName, int pushType) {
        ChatRoomHttpClient.getInstance().createRoom(
                DemoCache.getAccountId(),
                roomName,
                pushType,
                ChatRoomNetConstants.ROOM_TYPE_CHAT,
                new ChatRoomHttpClient.ChatRoomHttpCallback<VoiceRoomInfo>() {

                    @Override
                    public void onSuccess(VoiceRoomInfo roomInfo) {
                        if (roomInfo != null) {
                            roomInfo.setAudioQuality(DEFAULT_QUALITY);

                            AnchorRoomActivity.start(MainActivity.this, roomInfo);
                        } else {
                            ToastHelper.showToast(getString(R.string.crate_room_error));
                        }
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        if (TextUtils.isEmpty(errorMsg)) {
                            errorMsg = getString(R.string.params_error);
                        } else {
                            errorMsg = "服务器失败";
                        }
                        ToastHelper.showToast("创建失败:" + (!NetworkUtils.isNetworkConnected(MainActivity.this) ? "网络错误" : errorMsg));
                    }
                });
    }
}