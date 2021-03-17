package com.example.william.my.module.open.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.module.open.R;
import com.example.william.my.module.open.bean.SectionBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BRVAHAdapter2 extends BaseSectionQuickAdapter<SectionBean, BaseViewHolder> {

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */

    public BRVAHAdapter2(@NotNull List<SectionBean> data) {
        super(R.layout.open_item_recycler, R.layout.open_item_recycler, data);
    }

    /**
     * 在此方法中设置head item数据
     */
    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull SectionBean sectionBean) {
        helper.setText(R.id.item_textView, "Head : " + sectionBean.getPosition());
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, SectionBean sectionBean) {
        helper.setText(R.id.item_textView, "Content : " + sectionBean.getPosition());
    }
}
