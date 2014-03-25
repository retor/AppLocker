package com.retor.AppLocker;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> _fragments) {
        super(fm);
        fragments = _fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Title One";
            case 1:
                return "Title Two";
            default:
                return null;
        }
    }

}
