package com.netease.audioroom.demo.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.LiveBaseAdapter;
import com.netease.audioroom.demo.widget.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 麦位点击弹窗
 */
public class SeatMenuDialog extends BottomBaseDialog {

    private Activity activity;
    private final List<String> items = new ArrayList<>();
    private OnItemClickListener<String> onItemClickListener;

    public SeatMenuDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public SeatMenuDialog setOnItemClickListener(OnItemClickListener<String> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    protected void renderTopView(FrameLayout parent) {
    }

    @Override
    protected boolean enableTop() {
        return false;
    }

    @Override
    protected void renderBottomView(FrameLayout parent) {
        RecyclerView rvMemberList = new RecyclerView(getContext());
        rvMemberList.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        rvMemberList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMemberList.setAdapter(new LiveBaseAdapter<String>(getContext(), items) {

            @Override
            protected int getLayoutId(int viewType) {
                return R.layout.view_item_list_text;
            }

            @Override
            protected LiveViewHolder onCreateViewHolder(View itemView) {
                return new LiveViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(LiveViewHolder holder, String itemData) {
                super.onBindViewHolder(holder, itemData);
                TextView tvName = holder.getView(R.id.tv_item);
                tvName.setText(itemData);
                holder.itemView.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(itemData);
                    }
                    dismiss();
                });
            }
        });
        parent.addView(rvMemberList, layoutParams);
    }


    public void show(@NonNull FragmentManager manager, List<String> items) {
        if (items != null && !items.isEmpty()) {
            this.items.addAll(items);
        }
        super.show();
    }
}
