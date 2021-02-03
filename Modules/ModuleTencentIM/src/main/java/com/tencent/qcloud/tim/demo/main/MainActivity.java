package com.tencent.qcloud.tim.demo.main;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.base.BaseActivity;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;

public class MainActivity extends BaseActivity implements ConversationManagerKit.MessageUnreadWatcher {

    private TextView mConversationBtn;
    private TextView mContactBtn;
    private TextView mProfileBtn;

    private TextView mMsgUnread;//未读消息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_main);
        initView();
    }

    private void initView() {
        mConversationBtn = findViewById(R.id.conversation);
        mContactBtn = findViewById(R.id.contact);
        mProfileBtn = findViewById(R.id.profile);
        mMsgUnread = findViewById(R.id.msg_total_unread);

        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, new ConversationFragment()).commitAllowingStateLoss();

        FileUtil.initPath(); // 从application移入到这里，原因在于首次装上app，需要获取一系列权限，如创建文件夹，图片下载需要指定创建好的文件目录，否则会下载本地失败，聊天页面从而获取不到图片、表情

        ConversationManagerKit.getInstance().addUnreadWatcher(this); // 未读消息监视器

        GroupChatManagerKit.getInstance(); //
    }

    public void tabClick(View view) {
        initTab();
        Fragment current = null;
        if (view.getId() == R.id.conversation_btn_group) {
            current = new ConversationFragment();
            mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
            mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.im_conversation_selected), null, null);
        } else if (view.getId() == R.id.contact_btn_group) {
            current = new ContactFragment();
            mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
            mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.im_contact_selected), null, null);
        } else if (view.getId() == R.id.myself_btn_group) {
            current = new ProfileFragment();
            mProfileBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
            mProfileBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.im_profile_selected), null, null);
        }
        if (current != null && !current.isAdded()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, current).commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    private void initTab() {
        mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.im_conversation_normal), null, null);
        mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.im_contact_normal), null, null);
        mProfileBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mProfileBtn.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.im_profile_normal), null, null);
    }

    /**
     * 未读消息
     */
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
    protected void onDestroy() {
        ConversationManagerKit.getInstance().destroyConversation();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
