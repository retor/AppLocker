package com.retor.AppLocker.retor4i;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import static android.app.ActivityManager.RunningAppProcessInfo;

/**
 * Created by retor on 25.03.2014.
 */
public class AppInfo {
    private String packageName;
    private Context context;
    private int uid;
    private String locationDir;
    private boolean isSystem = false;
    private Drawable icon;
    private PackageManager packageManager;
    private ActivityManager activityManager;
    private PackageInfo mPackageInfo;
    private String appLabel;
    private boolean check;
    private int pid;

    public AppInfo() {
    }

    public AppInfo(Context _context, RunningAppProcessInfo runningAppProcessInfo){
        context = _context;
        setPackageName(runningAppProcessInfo);
        setAppLabel(runningAppProcessInfo);
        setIcon(runningAppProcessInfo);
        setLocationDir(runningAppProcessInfo);
        setUid(runningAppProcessInfo);

    }
/*    public ArrayList<AppInfo> getListAppInfo(List<RunningAppProcessInfo> runningAppProcessInfo) {
        ArrayList<AppInfo> appInfo = new ArrayList<AppInfo>();
        for (RunningAppProcessInfo running:runningAppProcessInfo){
            AppInfo temp = new AppInfo(context, running);
            appInfo.add(temp);
        }
        return appInfo;
    }*/
    public ArrayList<AppInfo> getListAppInfo(List<RunningAppProcessInfo> runningAppProcessInfo, Context cont) {
    ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();

        for (RunningAppProcessInfo running:runningAppProcessInfo){
        AppInfo temp = new AppInfo(cont, running);
        appInfoList.add(temp);
        }

        return appInfoList;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(RunningAppProcessInfo runningAppProcessInfo) {
        packageName = runningAppProcessInfo.processName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(RunningAppProcessInfo runningAppProcessInfo) {
        uid = runningAppProcessInfo.uid;
    }

    public String getLocationDir() {
        return locationDir;
    }

    public void setLocationDir(RunningAppProcessInfo runningAppProcessInfo) {
        packageManager = context.getPackageManager();
        try {
            locationDir = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).sourceDir.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(RunningAppProcessInfo runningAppProcessInfo) {
        packageManager = context.getPackageManager();
        try {
            icon = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).loadIcon(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            icon = context.getResources().getDrawable(android.R.drawable.ic_delete);
        }
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(RunningAppProcessInfo runningAppProcessInfo) {
        packageManager = context.getPackageManager();
        try {
            appLabel = packageManager.getApplicationInfo(runningAppProcessInfo.processName,0).loadLabel(packageManager).toString();// (runningAppProcessInfo.processName, PackageManager.GET_ACTIVITIES).loadLabel(packageManager).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appLabel = "no name";
        }
    }

    public int getPid() {
        return pid;
    }

    public void setPid(RunningAppProcessInfo runningAppProcessInfo) {
        pid = runningAppProcessInfo.pid;
    }

    public boolean isChecked() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
