package com.retor.AppLocker;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class Home extends FragmentActivity {

    ViewPager pager;
    ListApps listApps;
    ListAppsAdapter appsAdapter;
    PackageManager pm;
    ArrayList<ApplicationInfo> appList;
    ArrayList<Fragment> fragments;
    ViewPagerAdapter pagerAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pm = getPackageManager();
        pager = new ViewPager(getApplicationContext());
        appList = new ArrayList<ApplicationInfo>(pm.getInstalledApplications(PackageManager.GET_ACTIVITIES));

        appsAdapter = new ListAppsAdapter(getApplicationContext(), appList, R.layout.app, pm);
        listApps = new ListApps();
        listApps.setListAdapter(appsAdapter);

        fragments = new ArrayList<Fragment>();
        fragments.add(listApps);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
        setContentView(pager);
    }
}
