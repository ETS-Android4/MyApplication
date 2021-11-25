package com.example.william.my.module.opensource.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.opensource.bean.SectionBean;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BRVAHAdapter2 extends BaseSectionQuickAdapter<SectionBean, BaseViewHolder> {

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */

    public BRVAHAdapter2(@NonNull List<SectionBean> data) {
        super(R.layout.open_item_recycler, R.layout.open_item_recycler, data);
    }

    /**
     * 在此方法中设置head item数据
     */
    @Override
    protected void convertHeader(@NonNull BaseViewHolder helper, @NonNull SectionBean sectionBean) {
        helper.setText(R.id.item_textView, "Head : " + sectionBean.getPosition());
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, SectionBean sectionBean) {
        helper.setText(R.id.item_textView, "Content : " + sectionBean.getPosition());
    }
}
