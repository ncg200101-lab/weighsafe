package com.weigh.safemobile.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.weigh.safemobile.fragments2.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public  class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private static Fragment mFragmentPos0;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        if (fragment instanceof HomeFragment)
            mFragmentPos0 = fragment;

        mFragmentList.add(fragment);
    }

    public void replaceFragment(Fragment fragment, int Position) {
        mFragmentList.remove(0);
        mFragmentList.add(0, fragment);

        notifyDataSetChanged();
    }

    public void restoreFragment() {
        mFragmentList.set(0, mFragmentPos0);
        notifyDataSetChanged();
    }
}