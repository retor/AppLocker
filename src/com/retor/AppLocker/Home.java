package com.retor.AppLocker;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class Home extends FragmentActivity {

    ViewPager pager;
    ListApps listApps;
    ListApps temp;
    ListAppsAdapter appsAdapter;
    ArrayList<Fragment> fragments;
    ViewPagerAdapter pagerAdapter;
    PagerTabStrip pagerTab;

    PackageManager pm;
    List<PackageInfo> appList;
    List<PackageInfo> appListAuto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pm = getPackageManager();
        pager = (ViewPager)findViewById(R.id.viewpager);
        pagerTab = (PagerTabStrip)findViewById(R.id.pagertab);

        appList = new ArrayList<PackageInfo>();
        appList = pm.getInstalledPackages(getPackageManager().GET_ACTIVITIES); //getInstalledApplications(4);

        appListAuto = new ArrayList<PackageInfo>();
        appListAuto = catchAutoRun(appList);


        appsAdapter = new ListAppsAdapter(getApplicationContext(), appList, R.layout.app, pm);
        listApps = new ListApps();
        temp = new ListApps();
        listApps.setListAdapter(appsAdapter);
        temp.setListAdapter(new ListAppsAdapter(getApplicationContext(),appListAuto, R.layout.app, getPackageManager()));

        fragments = new ArrayList<Fragment>();
        fragments.add(0, listApps);
        fragments.add(1,temp);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
    }

    private List<PackageInfo> catchAutoRun(List<PackageInfo> packageInfos){
        ArrayList<PackageInfo> autoruns = new ArrayList<PackageInfo>();

        for (PackageInfo pi : packageInfos){
            PackageInfo temp;
            int i = 0;
            try {
                temp = getPackageManager().getPackageInfo(pi.packageName, getPackageManager().GET_PERMISSIONS);
                String[] p = temp.requestedPermissions;
                if (p!=null) {
                    for (String t : p) {
                        if (t.contains("android.permission.RECEIVE_BOOT_COMPLETED")) {
                            autoruns.add(i, pi);
                            i = i++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return autoruns;
    }
}
