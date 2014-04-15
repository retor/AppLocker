package com.retor.AppLocker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import com.retor.AppLocker.retor4i.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class Home extends FragmentActivity{

    ViewPager pager;
    ListApps listApps;
    ListApps listAppsAuto;
    ListLunchedApps listTasks;
    ListAppsAdapter appsAdapter;
    ArrayList<Fragment> fragments;
    ViewPagerAdapter pagerAdapter;
    PagerTabStrip pagerTab;

    PackageManager pm;
    List<PackageInfo> appList;
    List<PackageInfo> appListAuto;
    Context context;
    List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList;
    LunchedAdapter luadapter;

    ArrayList<AppInfo> appInfos;
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
        pagerTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        pagerTab.setFocusable(false);
        pagerTab.setMotionEventSplittingEnabled(true);

        appList = new ArrayList<PackageInfo>();
        appList = getAppList();
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        runningAppProcessInfoList = new ArrayList<ActivityManager.RunningAppProcessInfo>();
        runningAppProcessInfoList = am.getRunningAppProcesses();


        appListAuto = new ArrayList<PackageInfo>();
        appListAuto = catchAutoRun(appList);

        appsAdapter = new ListAppsAdapter(getApplicationContext(), appList, R.layout.app, pm);
        listApps = new ListApps();

        listAppsAuto = new ListApps();
        listTasks = new ListLunchedApps();
        listApps.setListAdapter(appsAdapter);
        appInfos = new ArrayList<AppInfo>();
        for (ActivityManager.RunningAppProcessInfo running:runningAppProcessInfoList){
            AppInfo appInfo = new AppInfo(getApplicationContext(), running.processName);
        }
        luadapter = new LunchedAdapter(pm, getApplicationContext(), appInfos, R.layout.app);
        //luadapter = new LunchedAdapter(getApplicationContext(), runningAppProcessInfoList, R.layout.app, getPackageManager());
        luadapter.notifyDataSetChanged();
		listTasks.setListAdapter(luadapter);

        listAppsAuto.setListAdapter(new ListAppsAdapter(getApplicationContext(),appListAuto, R.layout.app, getPackageManager()));

        fragments = new ArrayList<Fragment>();
        fragments.add(0, listApps);
        fragments.add(1, listAppsAuto);
        fragments.add(2, listTasks);
				

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);

    }

    private List<PackageInfo> getAppList(){
        PackageManager pm = getPackageManager();
        List<PackageInfo> pi = new ArrayList<PackageInfo>();
        pi = pm.getInstalledPackages(0);
        return pi;
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
