package com.example.william.my.module.router.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.exception.HandlerException;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.adapter.RouterRecyclerAdapter;
import com.example.william.my.module.router.item.RouterItem;

@Route(path = ARouterPath.Fragment.FragmentBasicRecycler)
public class RouterRecyclerFragment extends BaseRecyclerFragment<RouterItem> {

    @Override
    protected void initRecyclerData(Bundle savedInstanceState) {
        super.initRecyclerData(savedInstanceState);
    }

    @Override
    public BaseQuickAdapter<RouterItem, BaseViewHolder> getAdapter() {
        return new RouterRecyclerAdapter();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            onDataSuccess(arguments.getParcelableArrayList("router"), false);
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        super.onItemClick(adapter, view, position);
        RouterItem item = (RouterItem) adapter.getData().get(position);
        try {
            ARouter.getInstance().build(item.mRouterPath).navigation();
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean canRefresh() {
        return false;
    }
}
