package com.example.william.my.module.util.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LanguageUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.util.R;

import java.util.Locale;

@Route(path = ARouterPath.Util.Util_Language)
public class LanguageActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        showResponse(getResources().getString(R.string.util_app_name));
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        //LanguageUtils.applyLanguage(
        //        LanguageUtils.getContextLanguage(LanguageActivity.this).equals(Locale.CHINA) ? Locale.US : Locale.CHINA, false
        //);
        LanguageUtils.applyLanguage(
                LanguageUtils.getCurrentLocale().equals(Locale.CHINA) ? Locale.US : Locale.CHINA
        );
    }
}