package com.retor.AppLocker;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class Home extends FragmentActivity {

    ViewPager pager;
    ListApps listApps;
    ListAppsAdapter appsAdapter;
    ArrayList<Fragment> fragments;
    ViewPagerAdapter pagerAdapter;
    PagerTabStrip pagerTab;

    PackageManager pm;
    ArrayList<ApplicationInfo> appList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pm = getPackageManager();
        pager = (ViewPager)findViewById(R.id.viewpager);
        pagerTab = (PagerTabStrip)findViewById(R.id.pagertab);
        appList = new ArrayList<ApplicationInfo>(pm.getInstalledApplications(0));

        appsAdapter = new ListAppsAdapter(getApplicationContext(), appList, R.layout.app, pm);
        listApps = new ListApps();
        listApps.setListAdapter(appsAdapter);

        fragments = new ArrayList<Fragment>();
        fragments.add(0, listApps);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
    }
}
