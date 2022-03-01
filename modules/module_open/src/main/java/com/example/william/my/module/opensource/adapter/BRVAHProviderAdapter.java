package com.example.william.my.module.opensource.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseProviderMultiAdapter;

import java.util.List;

/**
 * BaseProviderMultiAdapter
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BRVAHProviderAdapter extends BaseProviderMultiAdapter<String> {

    public BRVAHProviderAdapter() {
        // 注册 Provider
        addItemProvider(new BRVAHProvider());
        addItemProvider(new BRVAHProvider2());
    }


    /**
     * 自行根据数据、位置等信息，返回 item 类型
     */
    @Override
    protected int getItemType(@NonNull List<? extends String> list, int i) {
        // 根据返回的 type 分别设置数据
        if (i % 2 == 0) {
            return 999;
        }
        return 0;
    }
}
