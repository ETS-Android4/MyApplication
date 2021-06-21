package com.example.william.my.module.widget.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.william.my.core.widget.bottomsheet.ViewPagerBottomSheetDialogFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryVarFragment;
import com.example.william.my.module.fragment.RecyclerFragment;
import com.example.william.my.module.widget.R;
import com.example.william.my.module.widget.adapter.ViewPagerFragmentAdapter;

import java.util.Arrays;

/**
 * https://github.com/JiangAndroidwork/BottomSheetViewPager
 */
public class MyBottomSheetDialog extends ViewPagerBottomSheetDialogFragment {

    private final Fragment[] mFragments = new Fragment[]{
            new PrimaryFragment(),
            new PrimaryVarFragment(),
            new RecyclerFragment()};

    @Override
    protected int getLayout() {
        return R.layout.widget_dialog_bottom_sheet;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager mViewPager = view.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(mFragments.length);
        mViewPager.setAdapter(new ViewPagerFragmentAdapter(getChildFragmentManager(), Arrays.asList(mFragments), false));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                onPageChange(mViewPager);
            }
        });
    }
}
