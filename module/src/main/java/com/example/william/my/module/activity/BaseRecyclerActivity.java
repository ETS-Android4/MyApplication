package com.example.william.my.module.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.router.fragment.RouterRecyclerFragment;
import com.example.william.my.module.router.item.RouterItem;

import java.util.ArrayList;

public abstract class BaseRecyclerActivity extends BaseFragmentActivity {

    @Override
    protected Fragment setFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("router", buildRouter());
        RouterRecyclerFragment fragment = new RouterRecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    protected abstract ArrayList<RouterItem> buildRouter();
}
