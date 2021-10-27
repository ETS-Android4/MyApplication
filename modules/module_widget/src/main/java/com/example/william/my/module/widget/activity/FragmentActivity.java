package com.example.william.my.module.widget.activity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.fragment.BasicRecyclerFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryVarFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.widget.R;

/**
 * onAttach -> onCreate -> onActivityCreated -> onStart -> onResume
 */
@Route(path = ARouterPath.Widget.Widget_Fragment)
public class FragmentActivity extends BaseActivity implements View.OnClickListener {

    private final String[] mTitles = new String[]{"首页", "列表", "消息"};
    private final int[] mButtons = new int[]{R.id.fragment_button1, R.id.fragment_button2, R.id.fragment_button3};
    private final Fragment[] mFragments = new Fragment[]{
            new PrimaryFragment(),
            new PrimaryVarFragment(),
            new BasicRecyclerFragment()};

    private final Fragment[] mARouterFragments = new Fragment[]{
            (Fragment) ARouter.getInstance().build(ARouterPath.Fragment.FragmentPrimary).navigation(),
            (Fragment) ARouter.getInstance().build(ARouterPath.Fragment.FragmentPrimaryDark).navigation(),
            (Fragment) ARouter.getInstance().build(ARouterPath.Fragment.FragmentBasicRecycler).navigation()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_activity_fragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frameLayout, mFragments[0]);
        transaction.commit();

        for (int i = 0; i < mButtons.length; i++) {
            TextView mTextView = findViewById(mButtons[i]);
            mTextView.setText(mTitles[i]);
            mTextView.setOnClickListener(this);
        }

        findViewById(mButtons[0]).setSelected(true);

        //initRadioButtons();
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mButtons.length; i++) {
            if (mButtons[i] == v.getId()) {
                switchTo(i);
            }
        }
    }

    private void switchTo(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frameLayout, mFragments[position]);
        //添加到回退栈,按返回键返回上一个fragment
        //transaction.addToBackStack(null);
        // 事务提交
        transaction.commit();

        for (int radioButton : mButtons) {
            TextView mRadioButton = findViewById(radioButton);
            mRadioButton.setSelected(false);
        }
        TextView mRadioButton = findViewById(mButtons[position]);
        mRadioButton.setSelected(true);
    }

    private void initRadioButtons() {
        for (int radioButton : mButtons) {
            TextView mRadioButton = findViewById(radioButton);
            //获得每个drawable对象
            Drawable[] drawables = mRadioButton.getCompoundDrawables();
            // 获得一个矩阵大小，图片宽高的 1/4
            Rect rect = new Rect(0, 0, drawables[1].getMinimumWidth() / 5, drawables[1].getMinimumHeight() / 5);
            //给图片设置矩阵大小
            drawables[1].setBounds(rect);
            // 设置给按钮的图片
            mRadioButton.setCompoundDrawables(null, drawables[1], null, null);
        }
    }
}
