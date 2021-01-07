package com.tencent.qcloud.tim.demo.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tencent.imsdk.v2.V2TIMSignalingInfo;
import com.tencent.liteav.model.CallModel;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.qcloud.tim.demo.BaseActivity;
import com.tencent.qcloud.tim.demo.DemoApplication;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.contact.ContactFragment;
import com.tencent.qcloud.tim.demo.conversation.ConversationFragment;
import com.tencent.qcloud.tim.demo.profile.ProfileFragment;
import com.tencent.qcloud.tim.demo.scenes.ScenesFragment;
import com.tencent.qcloud.tim.demo.utils.Constants;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;

public class MainActivity extends BaseActivity implements ConversationManagerKit.MessageUnreadWatcher {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mConversationBtn;
    private TextView mContactBtn;
    private TextView mProfileSelfBtn;
    private TextView mScenesBtn;
    private TextView mMsgUnread;
    private View mLastTab;

    private CallModel mCallModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DemoLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DemoLog.i(TAG, "onNewIntent");
        mCallModel = (CallModel) intent.getSerializableExtra(Constants.CHAT_INFO);
    }

    private void initView() {
        setContentView(R.layout.main_activity);
        mConversationBtn = findViewById(R.id.conversation);
        mContactBtn = findViewById(R.id.contact);
        mProfileSelfBtn = findViewById(R.id.mine);
        mScenesBtn = findViewById(R.id.scenes);
        mMsgUnread = findViewById(R.id.msg_total_unread);
        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, new ConversationFragment()).commitAllowingStateLoss();
        FileUtil.initPath(); // 从application移入到这里，原因在于首次装上app，需要获取一系列权限，如创建文件夹，图片下载需要指定创建好的文件目录，否则会下载本地失败，聊天页面从而获取不到图片、表情

        // 未读消息监视器
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
        GroupChatManagerKit.getInstance();
        if (mLastTab == null) {
            mLastTab = findViewById(R.id.conversation_btn_group);
        } else {
            // 初始化时，强制切换tab到上一次的位置
            View tmp = mLastTab;
            mLastTab = null;
            tabClick(tmp);
            mLastTab = tmp;
        }
    }

    public void tabClick(View view) {
        DemoLog.i(TAG, "tabClick last: " + mLastTab + " current: " + view);
        if (mLastTab != null && mLastTab.getId() == view.getId()) {
            return;
        }
        mLastTab = view;
        resetMenuState();
        Fragment current = null;
        switch (view.getId()) {
            case R.id.conversation_btn_group:
                current = new ConversationFragment();
                mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.conversation_selected), null, null);
                break;
            case R.id.contact_btn_group:
                current = new ContactFragment();
                mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.contact_selected), null, null);
                break;
            case R.id.scenes_btn_group:
                current = new ScenesFragment();
                mScenesBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mScenesBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.live_selected), null, null);
                break;
            case R.id.myself_btn_group:
                current = new ProfileFragment();
                mProfileSelfBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mProfileSelfBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.myself_selected), null, null);
                break;
            default:
                break;
        }

        if (current != null && !current.isAdded()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, current).commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        } else {
            DemoLog.w(TAG, "fragment added!");
        }
    }

    private void resetMenuState() {
        mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.conversation_normal), null, null);
        mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.contact_normal), null, null);
        mProfileSelfBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mProfileSelfBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.myself_normal), null, null);
        mScenesBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mScenesBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.live_normal), null, null);
    }


    @Override
    public void updateUnread(int count) {
        if (count > 0) {
            mMsgUnread.setVisibility(View.VISIBLE);
        } else {
            mMsgUnread.setVisibility(View.GONE);
        }
        String unreadStr = "" + count;
        if (count > 100) {
            unreadStr = "99+";
        }
        mMsgUnread.setText(unreadStr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onStart() {
        DemoLog.i(TAG, "onStart");
        super.onStart();
        initView();
    }

    @Override
    protected void onResume() {
        DemoLog.i(TAG, "onResume");
        super.onResume();
        if (mCallModel != null) {
            TRTCAVCallImpl impl = (TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(DemoApplication.instance());
            impl.stopCall();
            final V2TIMSignalingInfo info = new V2TIMSignalingInfo();
            info.setInviteID(mCallModel.callId);
            info.setInviteeList(mCallModel.invitedList);
            info.setGroupID(mCallModel.groupId);
            info.setInviter(mCallModel.sender);
            info.setData(mCallModel.data);
            ((TRTCAVCallImpl) (TRTCAVCallImpl.sharedInstance(DemoApplication.instance()))).processInvite(
                    info.getInviteID(), info.getInviter(), info.getGroupID(), info.getInviteeList(), info.getData());
            mCallModel = null;
        }
    }

    @Override
    protected void onPause() {
        DemoLog.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        DemoLog.i(TAG, "onStop");
        ConversationManagerKit.getInstance().destroyConversation();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DemoLog.i(TAG, "onDestroy");
        mLastTab = null;
        super.onDestroy();
    }

}
