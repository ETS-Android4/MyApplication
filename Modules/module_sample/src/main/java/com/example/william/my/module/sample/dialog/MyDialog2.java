package com.example.william.my.module.sample.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.william.my.library.dialog.BaseDialogFragment;
import com.example.william.my.module.sample.R;

public class MyDialog2 extends BaseDialogFragment {

    /**
     * 通过onCreateView返回一个view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basics_layout_response, container);
    }
}
