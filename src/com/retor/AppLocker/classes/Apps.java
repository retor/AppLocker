package com.retor.AppLocker.classes;

import android.content.pm.PackageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Антон on 07.05.2014.
 */
public class Apps extends PackageInfo implements Serializable {

    private boolean check;

    public Apps() {
        check = false;
    }

    public static ArrayList<Apps> makeApps(List<PackageInfo> packageInfo) {
        ArrayList<Apps> arrayApps = new ArrayList<Apps>();
        Apps apps;
        if (packageInfo != null) {
            for (PackageInfo tmpInfo : packageInfo) {
                apps = new Apps();
                apps.applicationInfo = tmpInfo.applicationInfo;
                apps.packageName = tmpInfo.packageName;
                apps.permissions = tmpInfo.permissions;
                apps.requestedPermissions = tmpInfo.requestedPermissions;
                apps.activities = tmpInfo.activities;
                apps.firstInstallTime = tmpInfo.firstInstallTime;
                apps.gids = tmpInfo.gids;
                apps.configPreferences = tmpInfo.configPreferences;
                apps.providers = tmpInfo.providers;
                apps.receivers = tmpInfo.receivers;
                arrayApps.add(apps);
            }
        }
        return arrayApps;
    }



    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
