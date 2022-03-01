package com.example.william.my.module.opensource.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.opensource.R;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BRVAHAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public BRVAHAdapter() {
        super(R.layout.open_item_recycler);
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_textView, item);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item, List<?> payloads) {
        if (payloads.isEmpty()) {
            super.convert(holder, item, payloads);
        } else {
            holder.setText(R.id.item_textView, payloads.get(0).toString());
        }
    }
}
