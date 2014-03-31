package com.retor.AppLocker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.*;
import android.widget.*;
import android.view.*;
import android.app.*;

public class Home extends FragmentActivity{

    ViewPager pager;
    ListApps listApps;
    ListApps listAppsAuto;
    ListApps listTasks;
    ListAppsAdapter appsAdapter;
    ArrayList<Fragment> fragments;
    ViewPagerAdapter pagerAdapter;
    PagerTabStrip pagerTab;

    PackageManager pm;
    List<PackageInfo> appList;
    List<PackageInfo> appListAuto;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = getApplicationContext();
        pm = getPackageManager();
        pager = (ViewPager)findViewById(R.id.viewpager);
        pagerTab = (PagerTabStrip)findViewById(R.id.pagertab);
        int color = 0x00FF8800;
        pagerTab.setTabIndicatorColor(color);
        pagerTab.setTextSize(TypedValue.COMPLEX_UNIT_PX, 14);
        pagerTab.setFocusable(false);
        pagerTab.setMotionEventSplittingEnabled(true);
			/*	ListView lv=;
				lv.setOnItemClickListener(new OnItemClickListener(){
								@Override
						    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT);

								}
						});*/

        appList = new ArrayList<PackageInfo>();
        appList = pm.getInstalledPackages(getPackageManager().GET_ACTIVITIES); //getInstalledApplications(4);

        appListAuto = new ArrayList<PackageInfo>();
        appListAuto = catchAutoRun(appList);

        appsAdapter = new ListAppsAdapter(getApplicationContext(), appList, R.layout.app, pm);
        listApps = new ListApps();

        listAppsAuto = new ListApps();
        listTasks = new ListApps();
        listApps.setListAdapter(appsAdapter);
				

        listAppsAuto.setListAdapter(new ListAppsAdapter(getApplicationContext(),appListAuto, R.layout.app, getPackageManager()));

        fragments = new ArrayList<Fragment>();
        fragments.add(0, listApps);
        fragments.add(1, listAppsAuto);
        fragments.add(2, listTasks);
				

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listApps.setRetainInstance(true);
        listAppsAuto.setRetainInstance(true);
    }

}
