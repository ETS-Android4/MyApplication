package com.example.william.my.jet.activity;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.JetPack.JetPack_DataStore)
public class DataStoreActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();
        DataStore<Preferences> dataStore;

    }
}