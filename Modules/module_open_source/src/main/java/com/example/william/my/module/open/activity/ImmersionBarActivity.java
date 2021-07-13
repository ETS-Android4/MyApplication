package com.example.william.my.module.open.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = ARouterPath.OpenSource.OpenSource_ImmersionBar)
public class ImmersionBarActivity extends AppCompatActivity {

    public ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_immersion_bar);
        initAfterSetContentView();
    }

    protected boolean isUseImmersionBar() {
        return true;
    }

    /**
     * 解决布局与状态栏重叠问题，默认true
     */
    protected boolean fitsSystemWindows() {
        return true;
    }

    /**
     * 状态栏颜色
     */
    protected int getStatusBarColor() {
        return R.color.colorPrimary;
    }

    private void initAfterSetContentView() {
        if (isUseImmersionBar()) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar
                    .transparentStatusBar()  //透明状态栏，不写默认透明色
                    .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                    //Color
                    .statusBarColor(getStatusBarColor())     //状态栏颜色，不写默认透明色
                    .navigationBarColor(getStatusBarColor()) //导航栏颜色，不写默认黑色
                    .barColor(getStatusBarColor())  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
                    //Alpha
                    .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                    .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                    .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                    //Font
                    .statusBarDarkFont(true) //状态栏字体是深色，不写默认为亮色
                    //Windows
                    .fitsSystemWindows(fitsSystemWindows())//解决状态栏和布局重叠问题，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
                    //Keyboard
                    .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false
                    .init();

        }
    }
}