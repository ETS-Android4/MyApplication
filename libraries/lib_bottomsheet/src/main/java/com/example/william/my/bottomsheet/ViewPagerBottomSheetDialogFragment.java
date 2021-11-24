package com.example.william.my.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class ViewPagerBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public FrameLayout bottomSheet;
    private ViewPagerBottomSheetBehavior<FrameLayout> behavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new ViewPagerBottomSheetDialog(getContext(), getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPagerBottomSheetDialog dialog = (ViewPagerBottomSheetDialog) getDialog();
        if (dialog != null) {
            bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                behavior = ViewPagerBottomSheetBehavior.from(bottomSheet);
            }
        }
    }

    public void onPageChange(ViewPager viewPager) {
        if (viewPager != null && behavior != null) {
            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    behavior.updateScrollingChild();
                }
            });
        }
    }

    protected abstract int getLayout();
}