package com.tencent.qcloud.tim.demo.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.base.BaseActivity;
import com.tencent.qcloud.tim.demo.login.LoginActivity;
import com.tencent.qcloud.tim.demo.thirdpush.OfflineMessageDispatcher;
import com.tencent.qcloud.tim.demo.utils.Constants;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;

import static com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chat(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        DemoLog.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
        chat(intent);
    }

    @Override
    protected void onResume() {
        DemoLog.i(TAG, "onResume");
        super.onResume();
    }

    private void chat(Intent intent) {
        Bundle bundle = intent.getExtras();
        DemoLog.i(TAG, "bundle: " + bundle + " intent: " + intent);
        if (bundle == null) {
            startLoginActivity(null);
            return;
        }

        OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(intent);
        if (bean != null) {
            mChatInfo = new ChatInfo();
            mChatInfo.setType(bean.chatType);
            mChatInfo.setId(bean.sender);
            bundle.putSerializable(Constants.CHAT_INFO, mChatInfo);
            DemoLog.i(TAG, "offline mChatInfo: " + mChatInfo);
        } else {
            mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
            if (mChatInfo == null) {
                startLoginActivity(null);
                return;
            }
        }

        if (V2TIMManager.getInstance().getLoginStatus() == V2TIM_STATUS_LOGINED) {
            mChatFragment = new ChatFragment();
            mChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
        } else {
            startLoginActivity(bundle);
        }
    }

    private void startLoginActivity(Bundle bundle) {
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }
}
