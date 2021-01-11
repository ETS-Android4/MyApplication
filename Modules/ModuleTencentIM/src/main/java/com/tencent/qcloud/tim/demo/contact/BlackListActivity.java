package com.tencent.qcloud.tim.demo.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.base.BaseActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactListView;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

/**
 * 黑名单
 */
public class BlackListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_blacklist);

        initTitleAction();
        initView();
    }

    private void initTitleAction() {
        TitleBarLayout titleBar = findViewById(R.id.black_list_titlebar);
        titleBar.setTitle(getResources().getString(R.string.blacklist), TitleBarLayout.POSITION.LEFT);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.getRightGroup().setVisibility(View.GONE);
    }

    private void initView() {
        ContactListView list = findViewById(R.id.black_list);
        list.loadDataSource(ContactListView.DataSource.BLACK_LIST);
        list.setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                Intent intent = new Intent(BlackListActivity.this, FriendProfileActivity.class);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                startActivity(intent);
            }
        });
    }
}
