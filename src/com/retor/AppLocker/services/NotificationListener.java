package com.retor.AppLocker.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.retor.AppLocker.classes.Cons;

import java.util.ArrayList;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    ArrayList<String> blocks;

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        return super.getActiveNotifications();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blocks = fillArray();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        for (StatusBarNotification noti:getActiveNotifications()){
            if (blocks.contains(noti.getPackageName().toString())){
                cancelNotification(noti.getPackageName(), noti.getTag(), noti.getId());
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    private ArrayList<String> fillArray() {
        ArrayList<String> out = new ArrayList<String>();
        SharedPreferences preferences;
        if ((preferences = getSharedPreferences(Cons.APPS_LOCK, Context.MODE_MULTI_PROCESS)) != null) {
            for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                Object obj = val.getKey();
                if (obj != null) {
                    String tmp = obj.toString();
                    out.add(tmp);
                }
            }
        }
        return out;
    }
}
