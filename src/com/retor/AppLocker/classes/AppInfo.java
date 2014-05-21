package com.retor.AppLocker.classes;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static android.app.ActivityManager.RunningAppProcessInfo;

/**
 * Created by retor on 25.03.2014.
 */
public class AppInfo extends RunningAppProcessInfo implements Parcelable{

    private Context context;

    private boolean isSystem = false;
    private Drawable icon;
    private PackageManager packageManager;
    private String appLabel;
    private boolean check;



    public AppInfo() {
    }
      public AppInfo(Context _context, RunningAppProcessInfo runningAppProcessInfo){
          context = _context;
        if (runningAppProcessInfo!=null)
        super.uid = runningAppProcessInfo.uid;
          assert runningAppProcessInfo != null;
          super.pid = runningAppProcessInfo.pid;
        super.processName = runningAppProcessInfo.processName;
        super.importance = runningAppProcessInfo.importance;
        super.pkgList = runningAppProcessInfo.pkgList;
        super.importanceReasonCode = runningAppProcessInfo.importanceReasonCode;
        super.importanceReasonComponent = runningAppProcessInfo.importanceReasonComponent;
        super.importanceReasonPid = runningAppProcessInfo.importanceReasonPid;

        setAppLabel(runningAppProcessInfo);
        setIcon(runningAppProcessInfo);
    }

    public ArrayList<AppInfo> getListAppInfo(List<RunningAppProcessInfo> runningAppProcessInfo, Context cont) {
    ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
        for (RunningAppProcessInfo running:runningAppProcessInfo){
        AppInfo temp = new AppInfo(cont, running);
        appInfoList.add(temp);
        }
        return appInfoList;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(RunningAppProcessInfo runningAppProcessInfo) {
        packageManager = context.getPackageManager();
        try {
            icon = packageManager.getApplicationInfo(super.processName, PackageManager.GET_META_DATA).loadIcon(packageManager);
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

    public boolean isChecked() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
