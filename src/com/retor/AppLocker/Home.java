package com.retor.AppLocker;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.retor.AppLocker.activites.BlockActivity;
import com.retor.AppLocker.adapters.ListAppsAdapter;
import com.retor.AppLocker.adapters.LunchedAdapter;
import com.retor.AppLocker.adapters.ViewPagerAdapter;
import com.retor.AppLocker.classes.AppInfo;
import com.retor.AppLocker.classes.Apps;
import com.retor.AppLocker.classes.AppsToBlock;
import com.retor.AppLocker.classes.Cons;
import com.retor.AppLocker.fragments.DialogForgot;
import com.retor.AppLocker.fragments.ListApps;
import com.retor.AppLocker.fragments.ListAppsNew;
import com.retor.AppLocker.fragments.ListLunchedApps;
import com.retor.AppLocker.services.ListenService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.ActivityManager.RunningAppProcessInfo;
import static com.retor.AppLocker.classes.Apps.makeApps;

public class Home extends ActionBarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    ActivityManager am;
    ProgressDialog pd;
    //view pager
    private ViewPager pager;
    private ArrayList<Fragment> fragments;
    private PagerTabStrip pagerTab;
    private ViewPagerAdapter vpa;
    //fragments
    private ListApps listApps;
    private ListApps listAppsAuto;
    private ListLunchedApps listTasks;
    private ListAppsNew listik;
    //arrays
    private ArrayList<AppsToBlock> listApss;
    private ArrayList<Apps> testArray;
    private ArrayList<Apps> testArray1;
    private ArrayList<AppInfo> appInfos; //my class
    //other
    private PackageManager pm;
    private Context context;
    private SlidingMenu sm;
    private Typeface tf;
    private android.support.v7.app.ActionBar actionBar;
    //tests


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light);
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        SharedPreferences preferences = getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS);
        preferences.edit().commit();
        if (!preferences.getBoolean(Cons.APP_PREF_PASS_SET, false)){
            createDialog(Cons.MODE_NEW_PASSWORD, false, "First set");
        }else{
            createDialog(Cons.MODE_AUTH_APP, false, "Auth app");
        }
        getSharedPreferences(Cons.APPS_LOCK, MODE_MULTI_PROCESS).edit().commit();
        sendBroadcast(new Intent().setAction(BlockActivity.NORMAL));

        //set first parameters
        setContentView(R.layout.main);
        context = getApplicationContext();
        pm = getPackageManager();
        pager = (ViewPager) findViewById(R.id.viewpager);
        pagerTab = (PagerTabStrip) findViewById(R.id.pagertab);
        int color = 0x00FF8800;
        pagerTab.setTabIndicatorColor(color);
        pagerTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        pagerTab.setFocusable(false);
        pagerTab.setMotionEventSplittingEnabled(true);
        am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        String pathTTF = "fonts/ModernAntiqua.ttf";
        tf = Typeface.createFromAsset(getAssets(), pathTTF);

        initSlidingMenu();

        //create/init fragments
        listik = new ListAppsNew();
        listApps = new ListApps();
        listAppsAuto = new ListApps();
        listTasks = new ListLunchedApps();
        listApss = new ArrayList<AppsToBlock>();
        testArray = new ArrayList<Apps>();
        testArray1 = new ArrayList<Apps>();
        appInfos = new ArrayList<AppInfo>();
        fragments = new ArrayList<Fragment>();

        //ActionBar
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(this).show(this, null, "Loading...", true, true);
        testArray = new ArrayList<Apps>();
        new RequestInfo();
        vpa = new ViewPagerAdapter(getSupportFragmentManager(), fragments, getApplicationContext(), actionBar);
        pager.setOnPageChangeListener(this);


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        String firs = String.valueOf(testArray.size());
        menu.getItem(0).setTitle(firs);
        menu.getItem(0).setEnabled(false);
        return super.onCreatePanelMenu(featureId, menu);
    }

    private List<PackageInfo> getAppList() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> pi = new ArrayList<PackageInfo>();
        pi = pm.getInstalledPackages(0);
        return pi;
    }

    private List<PackageInfo> catchAutoRun(List<PackageInfo> packageInfos) {
        ArrayList<PackageInfo> autoruns = new ArrayList<PackageInfo>();
        for (PackageInfo pi : packageInfos) {
            PackageInfo temp;
            int i = 0;
            try {
                temp = getPackageManager().getPackageInfo(pi.packageName, getPackageManager().GET_PERMISSIONS);
                String[] p = temp.requestedPermissions;
                if (p != null) {
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
        listTasks.setRetainInstance(true);
    }

    public ArrayList<AppInfo> getListAppInfo(List<RunningAppProcessInfo> runningAppProcessInfo) {
        ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
        for (RunningAppProcessInfo running : runningAppProcessInfo) {
            AppInfo temp = new AppInfo(getApplicationContext(), running);
            appInfoList.add(temp);
        }
        return appInfoList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sm.toggle(true);
                break;
            case R.id.item2:
                Log.d("SendBroadcast", "Yes");
                Intent serviceIntent = new Intent(getApplicationContext(), ListenService.class);
                stopService(serviceIntent);
                startService(serviceIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //Log.d("SlidingMenu", "pressed" + String.valueOf(v.getId()));
        switch (v.getId()) {
            case R.id.textView1:
                pager.setCurrentItem(1);
                Toast.makeText(context, "Menu 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView2:
                Intent service = new Intent(this, ListenService.class);
                startService(service);
                Toast.makeText(context, "Menu 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView3:
                createDialog(Cons.MODE_NEW_WORD, true, "new word");
                Toast.makeText(context, "Menu 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView4:
                createDialog(Cons.MODE_NEW_PASS, true, "new password");
                Toast.makeText(context, "Menu 4", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        TextView textView = (TextView) findViewById(R.id.item1);
        textView.setText(getStringToBar(i));
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private void initSlidingMenu() {
        //SlidingMenu
        sm = new SlidingMenu(getApplicationContext());
        sm.setBehindWidth(450);
        sm.setMenu(R.layout.slidingmenu);
        sm.setMode(SlidingMenu.LEFT);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sm.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        });
        sm.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        });
        //sm.setOnClickListener(this);
        //OnClick for SlidingMenu
        TextView menu1 = (TextView) findViewById(R.id.textView1);
        menu1.setTypeface(tf);
        menu1.setOnClickListener(this);
        TextView menu2 = (TextView) findViewById(R.id.textView2);
        menu2.setTypeface(tf);
        menu2.setOnClickListener(this);
        TextView menu3 = (TextView) findViewById(R.id.textView3);
        menu3.setTypeface(tf);
        menu3.setOnClickListener(this);
        TextView menu4 = (TextView) findViewById(R.id.textView4);
        menu4.setTypeface(tf);
        menu4.setOnClickListener(this);
    }

    public String getStringToBar(int i) {
        switch (i) {
            case 0:
                return String.valueOf(listApss.size());
            case 1:
                return String.valueOf(testArray1.size());
            case 2:
                return String.valueOf(appInfos.size());
            case 3:
                return String.valueOf(testArray.size());
        }
        return null;
    }

    private class RequestInfo extends AsyncTask<Void, Void, Void> {

        public RequestInfo() {
            execute();
        }

        @Override
        protected void onPreExecute() {
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //create/set adapters for fragments
            listik.setListAdapter(new ListAppsAdapter(getApplicationContext(), listApss, R.layout.app, pm));
            listApps.setListAdapter(new ListAppsAdapter(getApplicationContext(), testArray, R.layout.app, pm));
            listAppsAuto.setListAdapter(new ListAppsAdapter(getApplicationContext(), testArray1, R.layout.app, pm));
            listTasks.setListAdapter(new LunchedAdapter(pm, context, appInfos, R.layout.app));
            //fill viewpager
            fragments.add(0, listik);
            fragments.add(1, listAppsAuto);
            fragments.add(2, listTasks);
            fragments.add(3, listApps);
            pager.setAdapter(vpa);
            pd.dismiss();
            getStringToBar(0);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            listApss = createAppList();
            testArray = makeApps(getAppList());
            testArray1 = makeApps(catchAutoRun(getAppList()));
            appInfos = getListAppInfo(am.getRunningAppProcesses());
            return null;
        }
    }

    private void createDialog(int mode, boolean cancelable, String title){
        DialogFragment di = new DialogForgot(getApplicationContext(), mode);
        di.setCancelable(cancelable);
        di.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        di.show(getSupportFragmentManager(), title);
    }

    private ArrayList<AppsToBlock> createAppList(){
        ArrayList<AppsToBlock> out = new ArrayList<AppsToBlock>();
        Intent filterIntent = new Intent(Intent.ACTION_MAIN);
        filterIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<ResolveInfo> appsFiltered = new ArrayList<ResolveInfo>();
        appsFiltered = (ArrayList<ResolveInfo>)pm.queryIntentActivities(filterIntent, PackageManager.GET_RESOLVED_FILTER);
        Collections.sort(appsFiltered, new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo info:appsFiltered){
            AppsToBlock app = new AppsToBlock(info);
            out.add(app);
        }
        return out;
    }
}

