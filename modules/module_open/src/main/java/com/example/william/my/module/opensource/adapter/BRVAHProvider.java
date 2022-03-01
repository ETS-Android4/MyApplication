package com.example.william.my.module.opensource.adapter;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.opensource.R;

public class BRVAHProvider extends BaseItemProvider<String> {

    // item 类型
    @Override
    public int getItemViewType() {
        return 0;
    }

    // 返回 item 布局 layout
    @Override
    public int getLayoutId() {
        return R.layout.open_item_recycler;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, String s) {
        // 设置 item 数据
        baseViewHolder.setText(R.id.item_textView, s);
        baseViewHolder.setBackgroundColor(R.id.item_textView, ContextCompat.getColor(context, R.color.basics_colorPrimary));
    }
}
