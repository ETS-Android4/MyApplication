package com.tencent.qcloud.tim.demo.main;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.base.BaseActivity;
import com.tencent.qcloud.tim.demo.base.DemoApplication;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.SelectionActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private View mBaseView;

    private TextView mAccountView;
    private LineControllerView mNickNameView;
    private LineControllerView mSignatureView;
    private LineControllerView mModifyAllowTypeView;
    private final ArrayList<Integer> mJoinTypeIdList = new ArrayList<>();
    private final ArrayList<String> mJoinTypeTextList = new ArrayList<>();

    private int mJoinTypeIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.im_fragment_profile, container, false);
        initView();
        return mBaseView;
    }

    private void initView() {
        ImageView mUserIcon = mBaseView.findViewById(R.id.self_icon);

        mAccountView = mBaseView.findViewById(R.id.self_account);

        mNickNameView = mBaseView.findViewById(R.id.modify_nick_name);
        mNickNameView.setCanNav(true);
        mNickNameView.setOnClickListener(this);
        mSignatureView = mBaseView.findViewById(R.id.modify_signature);
        mSignatureView.setCanNav(true);
        mSignatureView.setOnClickListener(this);

        mModifyAllowTypeView = mBaseView.findViewById(R.id.modify_allow_type);
        mModifyAllowTypeView.setCanNav(true);
        mModifyAllowTypeView.setOnClickListener(this);

        mJoinTypeTextList.add(getResources().getString(R.string.allow_type_allow_any));
        mJoinTypeTextList.add(getResources().getString(R.string.allow_type_deny_any));
        mJoinTypeTextList.add(getResources().getString(R.string.allow_type_need_confirm));
        mJoinTypeIdList.add(V2TIMUserFullInfo.V2TIM_FRIEND_ALLOW_ANY);
        mJoinTypeIdList.add(V2TIMUserFullInfo.V2TIM_FRIEND_DENY_ANY);
        mJoinTypeIdList.add(V2TIMUserFullInfo.V2TIM_FRIEND_NEED_CONFIRM);

        mBaseView.findViewById(R.id.logout_btn).setOnClickListener(this);

        String selfUserID = V2TIMManager.getInstance().getLoginUser();

        // 获取用户信息
        V2TIMManager.getInstance().getUsersInfo(Collections.singletonList(selfUserID), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {

            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() == 0) {
                    return;
                }
                V2TIMUserFullInfo v2TIMUserFullInfo = v2TIMUserFullInfos.get(0);

                TUIKitConfigs.getConfigs().getGeneralConfig().setUserFaceUrl(v2TIMUserFullInfo.getFaceUrl());

                TUIKitConfigs.getConfigs().getGeneralConfig().setUserNickname(v2TIMUserFullInfo.getNickName());

                if (TextUtils.isEmpty(v2TIMUserFullInfo.getFaceUrl())) {
                    GlideEngine.loadImage(mUserIcon, R.drawable.default_user_icon);
                } else {
                    GlideEngine.loadImage(mUserIcon, Uri.parse(v2TIMUserFullInfo.getFaceUrl()));
                }

                //ID
                mAccountView.setText(String.format(getResources().getString(R.string.id), v2TIMUserFullInfo.getUserID()));

                //昵称
                mNickNameView.setContent(v2TIMUserFullInfo.getNickName());

                //签名
                mSignatureView.setContent(v2TIMUserFullInfo.getSelfSignature());

                //加我验证方式
                mModifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_need_confirm));
                if (v2TIMUserFullInfo.getAllowType() == V2TIMUserFullInfo.V2TIM_FRIEND_ALLOW_ANY) {
                    mModifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_allow_any));
                    mJoinTypeIndex = 0;
                } else if (v2TIMUserFullInfo.getAllowType() == V2TIMUserFullInfo.V2TIM_FRIEND_DENY_ANY) {
                    mModifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_deny_any));
                    mJoinTypeIndex = 1;
                } else if (v2TIMUserFullInfo.getAllowType() == V2TIMUserFullInfo.V2TIM_FRIEND_NEED_CONFIRM) {
                    mModifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_need_confirm));
                    mJoinTypeIndex = 2;
                } else {
                    mModifyAllowTypeView.setContent("");
                }
            }
        });

        initTitleAction();
    }

    private void initTitleAction() {
        TitleBarLayout titleBar = mBaseView.findViewById(R.id.self_info_title_bar);
        titleBar.getLeftGroup().setVisibility(View.GONE);
        titleBar.getRightGroup().setVisibility(View.GONE);
        titleBar.setTitle(getResources().getString(R.string.profile), TitleBarLayout.POSITION.MIDDLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.modify_nick_name) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.modify_nick_name));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mNickNameView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection(getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mNickNameView.setContent(text.toString());
                    updateProfile();
                }
            });
        } else if (v.getId() == R.id.modify_signature) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.modify_signature));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mSignatureView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection(getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mSignatureView.setContent(text.toString());
                    updateProfile();
                }
            });
        } else if (v.getId() == R.id.modify_allow_type) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.add_rule));
            bundle.putStringArrayList(TUIKitConstants.Selection.LIST, mJoinTypeTextList);
            bundle.putInt(TUIKitConstants.Selection.DEFAULT_SELECT_ITEM_INDEX, mJoinTypeIndex);
            SelectionActivity.startListSelection(getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mModifyAllowTypeView.setContent(mJoinTypeTextList.get((Integer) text));
                    mJoinTypeIndex = (Integer) text;
                    updateProfile();
                }
            });
        } else if (v.getId() == R.id.logout_btn) {
            new TUIKitDialog(getActivity())
                    .builder()
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setTitle("您确定要退出登录么？")
                    .setDialogWidth(0.75f)
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TUIKit.logout(new IUIKitCallBack() {

                                @Override
                                public void onSuccess(Object data) {
                                    logout();
                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {
                                    ToastUtil.toastLongMessage("logout fail: " + errCode + "=" + errMsg);
                                    logout();
                                }

                                private void logout() {
                                    BaseActivity.logout(DemoApplication.instance());
                                    if (getActivity() != null) {
                                        getActivity().finish();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
        }
    }

    private void updateProfile() {
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();

        // 昵称
        String nickName = mNickNameView.getContent();
        v2TIMUserFullInfo.setNickname(nickName);

        // 个性签名
        String signature = mSignatureView.getContent();
        v2TIMUserFullInfo.setSelfSignature(signature);

        // 加我验证方式
        int allowType = mJoinTypeIdList.get(mJoinTypeIndex);
        v2TIMUserFullInfo.setAllowType(allowType);

        // 设置用户信息
        V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess() {

            }
        });
    }

}
