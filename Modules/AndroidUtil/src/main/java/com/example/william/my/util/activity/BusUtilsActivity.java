package com.example.william.my.util.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.BusUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/Blankj/AndroidUtilCode/tree/master/plugin/bus-gradle-plugin
 */
@Route(path = ARouterPath.Util.Util_BusUtils)
public class BusUtilsActivity extends BaseResponseActivity {

    public static final String TAG_NO_PARAM = "TagNoParam";
    public static final String TAG_ONE_PARAM = "TagOneParam";
    public static final String TAG_NO_PARAM_STICKY = "TagNoParamSticky";

    @BusUtils.Bus(tag = TAG_NO_PARAM)
    public void noParamFun() {
        /* Do something */
    }

    @BusUtils.Bus(tag = TAG_ONE_PARAM)
    public void oneParamFun(String param) {
        /* Do something */
        mResponse.setText(param);
    }

    @BusUtils.Bus(tag = TAG_NO_PARAM_STICKY, sticky = true, threadMode = BusUtils.ThreadMode.MAIN)
    public void noParamStickyFun() {
        /* Do something */
    }

    @Override
    public void setOnClick() {
        BusUtils.post(TAG_NO_PARAM);// noParamFun() will receive
        BusUtils.post(TAG_ONE_PARAM, "param");// oneParamFun() will receive

        //BusUtils.postSticky(TAG_NO_PARAM_STICKY);
        //BusUtils.register(TAG_NO_PARAM_STICKY);// will invoke noParamStickyFun
        //BusUtils.removeSticky(TAG_NO_PARAM_STICKY);// When u needn't use the sticky, remove it
    }

    @Override
    public void onStart() {
        super.onStart();
        BusUtils.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusUtils.unregister(this);
        BusUtils.unregister(TAG_NO_PARAM_STICKY);
    }
}