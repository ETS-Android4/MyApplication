package com.example.william.my.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * https://blog.csdn.net/jhl122/article/details/86526710
 * https://github.com/JiangAndroidwork/BottomSheetViewPager
 * BottomSheetDialogFragment + ViewPager + Fragment + RecyclerView，BottomSheetBehavior 内的 ViewPager下的 RecyclerView 无法响应滑动
 */
public abstract class ViewPagerBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public FrameLayout bottomSheet;
    private ViewPagerBottomSheetBehavior<FrameLayout> behavior;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new ViewPagerBottomSheetDialog(getContext(), getTheme());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
