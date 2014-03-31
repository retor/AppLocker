package com.retor.AppLocker;


import android.support.v4.app.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.content.*;

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
