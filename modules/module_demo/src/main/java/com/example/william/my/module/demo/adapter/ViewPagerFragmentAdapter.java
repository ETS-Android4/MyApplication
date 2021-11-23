package com.example.william.my.module.demo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;

import java.util.List;

/**
 * FragmentPagerAdapter通过setAdapter做不到整体刷新。
 * FragmentPagerAdapter在调用destroy的时候，采用的是detach的方式，并未真正的销毁Fragment，仅仅是销毁了View，导致FragmentManager中仍旧保留正Fragment的缓存
 * FragmentStatePagerAdapter可以通过setAdapter做到整体刷新。
 * FragmentStatePagerAdapter在destroyItem的时候调用的是remove，这种对于没有添加到回退栈的Fragment操作来说，不仅会销毁view，还会销毁Fragment
 * <p>
 * Fragment no longer exists for key f0: index 0
 * https://blog.csdn.net/eydwyz/article/details/78624907
 */
@SuppressWarnings("deprecation")
public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitles;
    private final List<Fragment> mFragments;

    /**
     * 如果 behavior 的值为 BEHAVIOR_SET_USER_VISIBLE_HINT，
     * 那么当 Fragment 对用户的可见状态发生改变时，setUserVisibleHint 方法会被调用。
     * 如果 behavior 的值为 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ，
     * 那么只有当前选中的 Fragment 在 {@link Lifecycle.State#RESUMED} 状态 ，其他不可见的 Fragment 会被限制在 {@link Lifecycle.State#STARTED} 状态。
     */
    public ViewPagerFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> mFragments, boolean isNew) {
        // 兼容旧方式懒加载
        super(fm, isNew ? BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT : BEHAVIOR_SET_USER_VISIBLE_HINT);
        // 限制Fragment声明周期
        // 只有当前Fragment执行onResume()，其他Fragment声明周期限制在onStart()
        //super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragments = mFragments;
    }

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<String> titles, boolean isNew) {
        // 兼容旧方式懒加载
        super(fm, isNew ? BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT : BEHAVIOR_SET_USER_VISIBLE_HINT);
        // 限制Fragment声明周期
        // 只有当前Fragment执行onResume()，其他Fragment声明周期限制在onStart()
        //super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= mTitles.size()) {
            return "";
        }
        return mTitles.get(position);
    }
}
