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

import static android.app.ActivityManager.RunningAppProcessInfo;

public class Home extends FragmentActivity{
    //view pager
    ViewPager pager;
    ArrayList<Fragment> fragments;
    PagerTabStrip pagerTab;
    //fragments
    ListApps listApps;
    ListApps listAppsAuto;
    ListLunchedApps listTasks;
    //arrays
    List<PackageInfo> appList;
    List<PackageInfo> appListAuto;
    ArrayList<AppInfo> appInfos; //my class
    //other
    PackageManager pm;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set first parameters
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
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        //create Arrays
        appList = new ArrayList<PackageInfo>();
        appList = getAppList();
        appListAuto = new ArrayList<PackageInfo>();
        appListAuto = catchAutoRun(appList);
        appInfos = new ArrayList<AppInfo>();
        appInfos = getListAppInfo(am.getRunningAppProcesses());

        //create/init fragments
        listApps = new ListApps();
        listAppsAuto = new ListApps();
        listTasks = new ListLunchedApps();

        //create/set adapters for fragments
        listApps.setListAdapter(new ListAppsAdapter(getApplicationContext(), appList, R.layout.app, pm));
        listAppsAuto.setListAdapter(new ListAppsAdapter(getApplicationContext(),appListAuto, R.layout.app, pm));
        listTasks.setListAdapter(new LunchedAdapter(pm, context, appInfos, R.layout.app));

        //fill viewpager
        fragments = new ArrayList<Fragment>();
        fragments.add(0, listApps);
        fragments.add(1, listAppsAuto);
        fragments.add(2, listTasks);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
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

    public ArrayList<AppInfo> getListAppInfo(List<RunningAppProcessInfo> runningAppProcessInfo) {
        ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
        for (RunningAppProcessInfo running:runningAppProcessInfo){
            AppInfo temp = new AppInfo(getApplicationContext(), running);
            appInfoList.add(temp);
        }
        return appInfoList;
    }

}
