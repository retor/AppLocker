package com.retor.AppLocker.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter{
				
    ArrayList<Fragment> fragments;
    Context context;
	ActionBar actionBar;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> _fragments, Context context, ActionBar actionBar) {
        super(fm);
        fragments = _fragments;
        this.context = context;
        this.actionBar = actionBar;

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
                return "allPackages";
            case 1:
                return "bootUp";
            case 2:
                return "lunchedApps";
            default:
                return null;
        }
    }
}
