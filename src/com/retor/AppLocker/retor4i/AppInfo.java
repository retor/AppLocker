package com.retor.AppLocker.retor4i;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by retor on 25.03.2014.
 */
public class AppInfo {
    private String PackageName;
    private String Activity;
    private Context context;
    private int uid;
    private String locationDir;
    private boolean isSystem = false;
    private Drawable icon;
    private PackageManager packageManager;
    private ActivityManager activityManager;
    private PackageInfo mPackageInfo;

    public AppInfo() {
    }

    public ArrayList<AppInfo> getListAppInfo(List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo) {
        ArrayList<AppInfo> appInfo = new ArrayList<AppInfo>();
        for (ActivityManager.RunningAppProcessInfo running:runningAppProcessInfo){
            AppInfo temp = new AppInfo(running.processName);
            appInfo.add(temp);
        }
        return appInfo;
    }

    public AppInfo(String packageName) {
        PackageName = packageName;
        isSystem();
        setUid();
        setLocationDir();
        setIcon();
    }

    public AppInfo(Context context, String packageName) {
        this.context = context;
        packageManager = context.getPackageManager();
        activityManager = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        this.setPackageName(packageName);
        isSystem();
        setUid();
        setLocationDir();
        setIcon();
    }

    public String getPackageName() {
        return PackageName;
    }

    private void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public int getUid() {
        return uid;
    }

    private void setUid() {
        try {
            uid = packageManager.getPackageInfo(getPackageName(),packageManager.GET_META_DATA).applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getLocationDir() {
        return locationDir;
    }

    private void setLocationDir() {
        try {
            this.locationDir = packageManager.getPackageInfo(getPackageName(),packageManager.GET_META_DATA).applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isSystem() {
        try {
            PackageInfo sys = packageManager.getPackageInfo("android", PackageManager.GET_SIGNATURES);
            if (mPackageInfo != null && mPackageInfo.signatures != null &&
                    sys.signatures[0].equals(mPackageInfo.signatures[0])){
                isSystem = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            isSystem = false;
        }
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Drawable getIcon() {
        return icon;
    }

    private void setIcon() {
        try {
            this.icon = packageManager.getPackageInfo(getPackageName(),packageManager.GET_META_DATA).applicationInfo.loadIcon(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
