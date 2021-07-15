package com.netease.audioroom.demo.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.example.william.my.module.router.ARouterPath;
import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.ChatHelper;
import com.netease.audioroom.demo.ChatLoginManager;
import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.RoomListAdapter;
import com.netease.audioroom.demo.base.BaseActivity;
import com.netease.audioroom.demo.cache.DemoCache;
import com.netease.audioroom.demo.http.ChatRoomHttpClient;
import com.netease.audioroom.demo.http.ChatRoomNetConstants;
import com.netease.audioroom.demo.model.AccountInfo;
import com.netease.audioroom.demo.receiver.NetworkConnectChangedReceiver;
import com.netease.audioroom.demo.util.Network;
import com.netease.audioroom.demo.util.NetworkUtils;
import com.netease.audioroom.demo.widget.loadsir.callback.EmptyChatRoomListCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.EmptyMuteRoomListCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.FailureCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.NetErrCallback;
import com.netease.audioroom.demo.widget.loadsir.callback.TimeoutCallback;
import com.netease.audioroom.demo.widget.loadsir.core.LoadSir;
import com.netease.yunxin.android.lib.network.common.NetworkClient;
import com.netease.yunxin.nertc.model.bean.VoiceRoomInfo;

import java.util.ArrayList;

import static com.netease.yunxin.nertc.model.NERtcVoiceRoomDef.RoomAudioQuality.DEFAULT_QUALITY;

@Route(path = ARouterPath.NeRtc.Audio)
public class ChatRoomListActivity extends BaseActivity {

    private View mEmptyView;

    private RecyclerView mRecyclerView;

    private RoomListAdapter roomListAdapter;

    private final ArrayList<VoiceRoomInfo> dataSource = new ArrayList<>();

    @Override
    protected int getContentViewID() {
        return R.layout.activity_chat_room_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        initNim();
        initViews();
        fetchRoomList();
        watchNetWork();
    }

    private void initNim() {
        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Network network = Network.getInstance();
            network.setConnected(true);
        }
        //同一页面初始化
        LoadSir.beginBuilder()
                .addCallback(new FailureCallback())
                .addCallback(new EmptyChatRoomListCallback())
                .addCallback(new NetErrCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new EmptyChatRoomListCallback())
                .addCallback(new EmptyMuteRoomListCallback())
                .setDefaultCallback(FailureCallback.class)
                .commit();

        registerReceiver();

        NetworkClient.getInstance().configBaseUrl(BuildConfig.SERVER_BASE_URL)
                .appKey(BuildConfig.NIM_APP_KEY)
                .configDebuggable(true);

        DemoCache.init(this);//用户数据初始化

        ChatHelper.initPlayer(getApplicationContext());//播放器初始化

        ChatHelper.initIM(this, null);//IM 初始化
    }

    /**
     * 网络变化观察者
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGE");
        filter.addAction("android.net.conn.STATE_CHANGE");
        registerReceiver(new NetworkConnectChangedReceiver(), filter);
    }

    private void initViews() {
        mEmptyView = findViewById(R.id.tv_empty);

        roomListAdapter = new RoomListAdapter(this);
        roomListAdapter.setItemClickListener((model, position) -> {
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
        mRecyclerView.setAdapter(roomListAdapter);

        View toCreate = findViewById(R.id.iv_new_live);
        toCreate.setOnClickListener(v -> {
            getRandomName();
        });
    }

    private void watchNetWork() {
        onNetwork();
//        NetworkChange.getInstance().getNetworkLiveData().observe(this, network -> {
//            if (network != null && network.isConnected()) {
//                onNetwork();
//            } else {
//                showError();
//            }
//        });
    }

    // not need every time login. im auto login when network change
    private void onNetwork() {
        ChatHelper.loginIM(new ChatLoginManager.Callback() {
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
                        roomListAdapter.refreshList(dataSource);
                        showEmptyView();
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        if (dataSource.isEmpty()) {
                            showEmptyView();
                        }
                        Log.e("FetchRoomList", "errorMsg is " + errorMsg + ", errorCode is " + code);
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

                            AnchorRoomActivity.start(ChatRoomListActivity.this, roomInfo);
                        } else {
                            ToastUtils.showShort(getString(R.string.crate_room_error));
                        }
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        if (TextUtils.isEmpty(errorMsg)) {
                            errorMsg = getString(R.string.params_error);
                        } else {
                            errorMsg = "服务器失败";
                        }
                        ToastUtils.showShort("创建失败:" + (!NetworkUtils.isNetworkConnected(ChatRoomListActivity.this) ? "网络错误" : errorMsg));
                    }
                });
    }
}