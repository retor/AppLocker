package com.retor.AppLocker.classes;

import android.content.pm.ResolveInfo;

import java.io.Serializable;

/**
 * Created by Антон on 26.06.2014.
 */
public class AppsToBlock extends ResolveInfo implements Serializable {
    private boolean check;

    public AppsToBlock(){
        check = false;
    }

    public AppsToBlock(ResolveInfo orig){
        activityInfo = orig.activityInfo;
        serviceInfo = orig.serviceInfo;
        filter = orig.filter;
        priority = orig.priority;
        preferredOrder = orig.preferredOrder;
        match = orig.match;
        specificIndex = orig.specificIndex;
        labelRes = orig.labelRes;
        nonLocalizedLabel = orig.nonLocalizedLabel;
        icon = orig.icon;
        resolvePackageName = orig.resolvePackageName;
        check = false;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
