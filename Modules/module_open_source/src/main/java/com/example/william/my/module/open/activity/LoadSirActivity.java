package com.example.william.my.module.open.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * https://github.com/KingJA/LoadSir
 */
@Route(path = ARouterPath.OpenSource.OpenSource_LoadSir)
public class LoadSirActivity extends AppCompatActivity {

    private LoadService<?> loadService;//提示页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_response);
        LoadSir.beginBuilder()
                //.addCallback(new ErrorCallback())//'添加各种状态页
                //.setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                // 重新加载逻辑
            }
        });
        //loadService.showSuccess();//成功回调
        //loadService.showCallback(EmptyCallback.class);//其他回调
    }
}