package com.example.william.my.module.network.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.network.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.NetWork.NetWork_Glide)
public class GlideActivity extends BaseActivity {

    private static final String URL_IMG = "https://web.hycdn.cn/arknights/official/pic/20210401/8b683b7c01ebf0eb570370a48b655504.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);
    }
}