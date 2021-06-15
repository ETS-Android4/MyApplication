package com.example.william.my.module.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragment;
import com.example.william.my.module.R;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Fragment.FragmentRecycler)
public class RecyclerFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.basics_fragment_recycler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> mData = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            mData.add("POSITION " + i);
        }

        RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    public static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> mData;

        public RecyclerAdapter(List<String> data) {
            this.mData = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_item_recycler, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).textView.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        /**
         * 刷新闪烁
         * setHasStableId(true)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView;

            private ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_primary_textView);
            }
        }

        public void setData(List<String> data) {
            this.mData = data;
        }
    }

}
