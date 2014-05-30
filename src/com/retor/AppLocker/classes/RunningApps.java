package com.retor.AppLocker.classes;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashSet;

/**
 * Created by Антон on 14.04.2014.
 */
public class RunningApps {

    private final HashSet<String> mHomePackages = new HashSet<String>();
    private Context context;
    private PackageManager mPm;
    private PackageInfo mPackageInfo;
    // private ApplicationsState.AppEntry mAppEntry;

    public RunningApps() {
    }

    public RunningApps(Context context) {
        this.context = context;
        mPm = context.getPackageManager();
    }

    private boolean isThisASystemPackage() {
        try {
            PackageInfo sys = mPm.getPackageInfo("android", PackageManager.GET_SIGNATURES);
            return (mPackageInfo != null && mPackageInfo.signatures != null &&
                    sys.signatures[0].equals(mPackageInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /*private boolean handleDisableable(Button button) {
        boolean disableable = false;
        // Try to prevent the user from bricking their phone
        // by not allowing disabling of apps signed with the
        // system cert and any launcher app in the system.

        if (mHomePackages.contains(mAppEntry.info.packageName) || isThisASystemPackage()) {
            // Disable button for core system applications.
            button.setText("Disable");
        } else if (mAppEntry info.enabled) {
            button.setText("Disable1");
            disableable = true;
        } else {
            button.setText("Enable");
            disableable = true;
        }

        return disableable;
    }*/

    private void appDisable(String packageName) {
        boolean current;
    }
}
