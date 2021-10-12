package com.example.william.my.module.open.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseProviderMultiAdapter;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BRVAHAdapter3 extends BaseProviderMultiAdapter<String> {

    public BRVAHAdapter3() {
        addItemProvider(new BRVAHItemProvider());
    }

    @Override
    protected int getItemType(@NonNull List<? extends String> list, int i) {
        if (i % 2 == 0) {
            return 999;
        }
        return 0;
    }
}
