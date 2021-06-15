package com.example.william.my.module.widget.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.module.widget.R;
import com.example.william.my.module.widget.adapter.RecyclerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_dialog_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 1920;

        RecyclerView mRecyclerView = view.findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> mData = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            mData.add("POSITION " + i);
        }

        RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
