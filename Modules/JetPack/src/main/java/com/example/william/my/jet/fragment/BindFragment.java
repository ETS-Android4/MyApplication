package com.example.william.my.jet.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.william.my.jet.databinding.JetFragmentBindBinding;

public class BindFragment extends Fragment {

    private JetFragmentBindBinding bind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = JetFragmentBindBinding.inflate(inflater, container, false);
        return bind.getRoot();
    }

    @Override
    public void onDestroyView() {
        bind = null;
        super.onDestroyView();
    }
}
