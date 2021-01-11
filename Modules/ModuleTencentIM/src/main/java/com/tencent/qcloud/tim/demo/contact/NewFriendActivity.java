package com.tencent.qcloud.tim.demo.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.imsdk.v2.V2TIMFriendApplicationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.add.AddMoreActivity;
import com.tencent.qcloud.tim.demo.base.BaseActivity;
import com.tencent.qcloud.tim.demo.base.DemoApplication;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends BaseActivity {

    private TextView mEmptyView;
    private ListView mNewFriendLv;
    private NewFriendAdapter mAdapter;
    private final List<V2TIMFriendApplication> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_new_friend_activity);

        initTitleAction();
        initView();
    }

    private void initTitleAction() {
        TitleBarLayout titleBar = findViewById(R.id.new_friend_titlebar);
        titleBar.setTitle(getResources().getString(R.string.new_friend), TitleBarLayout.POSITION.LEFT);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitle(getResources().getString(R.string.add_friend), TitleBarLayout.POSITION.RIGHT);
        titleBar.getRightIcon().setVisibility(View.GONE);
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DemoApplication.instance(), AddMoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isGroup", false);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mNewFriendLv = findViewById(R.id.new_friend_list);
        mEmptyView = findViewById(R.id.empty_text);
        V2TIMManager.getFriendshipManager().getFriendApplicationList(new V2TIMValueCallback<V2TIMFriendApplicationResult>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(V2TIMFriendApplicationResult v2TIMFriendApplicationResult) {
                if (v2TIMFriendApplicationResult.getFriendApplicationList() != null) {
                    if (v2TIMFriendApplicationResult.getFriendApplicationList().size() == 0) {
                        mEmptyView.setText(getResources().getString(R.string.no_friend_apply));
                        mNewFriendLv.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                mNewFriendLv.setVisibility(View.VISIBLE);
                mList.clear();
                mList.addAll(v2TIMFriendApplicationResult.getFriendApplicationList());
                mAdapter = new NewFriendAdapter(NewFriendActivity.this, R.layout.contact_new_friend_item, mList);
                mNewFriendLv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
