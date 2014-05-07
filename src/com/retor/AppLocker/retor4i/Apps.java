package com.retor.AppLocker.retor4i;

import android.content.pm.PackageInfo;

/**
 * Created by Антон on 07.05.2014.
 */
public class Apps extends PackageInfo {

    private boolean check;

    public Apps(){
        check=false;
    }


     public static Apps makeApps(PackageInfo packageInfo){
         Apps apps = new Apps();;
        if (packageInfo!=null) {
            apps.applicationInfo = packageInfo.applicationInfo;
            apps.packageName = packageInfo.packageName;
            apps.permissions = packageInfo.permissions;
            apps.requestedPermissions = packageInfo.requestedPermissions;
            apps.activities = packageInfo.activities;
            apps.firstInstallTime = packageInfo.firstInstallTime;
            apps.gids = packageInfo.gids;
            apps.configPreferences = packageInfo.configPreferences;
            apps.providers = packageInfo.providers;
            apps.receivers = packageInfo.receivers;
        }
         return apps;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
