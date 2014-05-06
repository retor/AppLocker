package com.retor.AppLocker;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.retor.AppLocker.retor4i.AppInfo;

import java.util.ArrayList;
import java.util.List;

import static android.app.ActivityManager.RunningAppProcessInfo;

public class Home extends FragmentActivity implements View.OnClickListener {

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
    private Context context;
    private SlidingMenu sm;

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


        //SlidingMenu
        sm = new SlidingMenu(getApplicationContext());
        sm.setMode(SlidingMenu.LEFT);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sm.setBehindWidth(200);
        sm.setMenu(R.layout.slidingmenu);
        sm.setOnClickListener(this);
        //OnClick for SlidingMenu
        TextView menu1 = (TextView)findViewById(R.id.textView1);
        menu1.setOnClickListener(this);
        TextView menu2 = (TextView)findViewById(R.id.textView2);
        menu2.setOnClickListener(this);
        TextView menu3 = (TextView)findViewById(R.id.textView3);
        menu3.setOnClickListener(this);
        TextView menu4 = (TextView)findViewById(R.id.textView4);
        menu4.setOnClickListener(this);

        //ActionBar

        ActionBar actionBar=(ActionBar)getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Hahaha");


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                sm.toggle(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.i("SlidingMenu", "pressed" + String.valueOf(v.getId()));
        switch (v.getId()) {
            case R.id.textView1:
                pager.setCurrentItem(1);
                Toast.makeText(context, "Menu 1", Toast.LENGTH_SHORT).show();
            case R.id.textView2:
                Toast.makeText(context, "Menu 2", Toast.LENGTH_SHORT).show();
            case R.id.textView3:
                Toast.makeText(context, "Menu 3", Toast.LENGTH_SHORT).show();
            case R.id.textView4:
                Toast.makeText(context, "Menu 4", Toast.LENGTH_SHORT).show();
        }
    }
}

