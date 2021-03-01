package com.tencent.qcloud.tim.demo.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.base.DemoApplication;
import com.tencent.qcloud.tim.demo.contact.BlackListActivity;
import com.tencent.qcloud.tim.demo.contact.FriendProfileActivity;
import com.tencent.qcloud.tim.demo.contact.GroupListActivity;
import com.tencent.qcloud.tim.demo.contact.NewFriendActivity;
import com.tencent.qcloud.tim.demo.menu.Menu;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactListView;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

/**
 * 通讯录
 */
public class ContactFragment extends BaseFragment {

    private Menu mMenu;
    private View mBaseView;
    private ContactLayout mContactLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.im_fragment_contact, container, false);
        initViews();
        return mBaseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 初始化通讯录
        mContactLayout.initDefault();
    }

    private void initViews() {
        // 从布局文件中获取通讯录面板
        mContactLayout = mBaseView.findViewById(R.id.contact_layout);

        mContactLayout.getContactListView().setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                if (position == 0) {
                    Intent intent = new Intent(DemoApplication.instance(), NewFriendActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DemoApplication.instance().startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(DemoApplication.instance(), GroupListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DemoApplication.instance().startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(DemoApplication.instance(), BlackListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DemoApplication.instance().startActivity(intent);
                } else {
                    Intent intent = new Intent(DemoApplication.instance(), FriendProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                    DemoApplication.instance().startActivity(intent);
                }
            }
        });
        initTitleAction();
    }

    private void initTitleAction() {
        mMenu = new Menu(getActivity(), mContactLayout.getTitleBar(), Menu.MENU_TYPE_CONTACT);
        mContactLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenu.isShowing()) {
                    mMenu.hide();
                } else {
                    mMenu.show();
                }
            }
        });
    }
}
