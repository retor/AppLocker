package com.retor.AppLocker.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import com.retor.AppLocker.activitys.BlockActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Антон on 19.05.2014.
 */
public class ListenService extends Service {

    final String ALL = "applock";
    final String UNLOCK = "appsunlock";

    ScheduledExecutorService executor;
    private SharedPreferences preferences;
    Checking myTask;

    String TAG="ListenService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null && intent.getStringExtra("appname")!=null){
//            Log.d(TAG, intent.toString());
            String t = intent.getStringExtra("appname");
//            appsNotBlock.add(getValue(UNLOCK, t));
//            Log.d(TAG, "BLYA");
            startActivity(new Intent(getPackageManager().getLaunchIntentForPackage(t)));
//            myTask.addNot(appsNotBlock);
            Log.d(TAG, "Created with apps");
        }else{
            Log.d(TAG, "Created without");
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        Log.d(TAG, "Closed");
    }

    @Override
    public void onCreate() {
        super.onCreate();
/*        apps = openPref(ALL);
        appsNotBlock = openPref(UNLOCK);*/
        myTask = new Checking();
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(myTask, 30, 150, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "Cleaning");
                getSharedPreferences("appsunlock", MODE_MULTI_PROCESS).edit().clear().commit();
            }
        }, 10, 10, TimeUnit.MINUTES);
        executor.scheduleAtFixedRate(new Runnable() {
            private ArrayList<String> getRunningActivites(){
                ArrayList<String> out = new ArrayList<String>();
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                for (ActivityManager.RunningTaskInfo task:am.getRunningTasks(Integer.MAX_VALUE)){
                    String tmp = task.topActivity.getClassName();
                    if (tmp!=null)out.add(tmp);
                }
                return out;
            }
            @Override
            public void run() {
                ArrayList<String> pref;
                    if((pref=openPref(UNLOCK))!=null) {
                        for (String app : pref) {
                            if (!getRunningActivites().contains(getValue(UNLOCK, app))){
                                getSharedPreferences(UNLOCK, MODE_MULTI_PROCESS).edit().remove(app).commit();
                                Log.d(TAG, "remove app:" + app);
                            }
                        }
                    }
            }
        }, 1, 20, TimeUnit.SECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class Checking implements Runnable {

        ArrayList<String> appsCheck;
        ArrayList<String> appsNot;
        Context context;

        public Checking(){
            context=getApplicationContext();
            appsCheck = openPref(ALL);
            appsNot = openPref(UNLOCK);

        }

        private ArrayList<String> getRunningActivites(){
            ArrayList<String> out = new ArrayList<String>();
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningTaskInfo task:am.getRunningTasks(Integer.MAX_VALUE)){
                String tmp = task.topActivity.getClassName();
                if (tmp!=null)out.add(tmp);
            }
            return out;
        }

        private void fullCheck(){
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            if (appsCheck!=null) {
                for (String appC : appsCheck) {
                    if ((getRunningActivites().contains(getValue(ALL, appC)) && (!appsNot.contains(appC))) && am.getRunningTasks(1).get(0).topActivity.getClassName().contains(getValue(ALL, appC))) {
                        Log.d(TAG, "AppFinded: " + appC);
                        sendBroadcast(new Intent(BlockActivity.BLOCK).putExtra("appname", appC));
                    }
                }
            }
        }

        private void clearNot(){
            if(appsNot!=null) {
                appsNot = openPref(UNLOCK);
                for (String app : appsNot) {
                    if (!getRunningActivites().contains(getValue(UNLOCK, app))){
                        getSharedPreferences(UNLOCK, MODE_MULTI_PROCESS).edit().remove(app).commit();
                        Log.d(TAG, "remove app:" + app);
                    }
                }
            }
            appsNot = openPref(UNLOCK);
        }

/*        private boolean checkRunningApps(String appCheck) {
            if (appsNot != null) {
                if (appsNot.contains(appCheck)) {
                    Log.d(TAG, "app here: " + appCheck);
                    return true;
                }
            }
            return false;
        }*/

        @Override
        public void run() {
            Log.d(TAG, "Zapuskaem zadachu");
            fullCheck();
        }
    }

    private ArrayList<String> openPref(String pref){
        ArrayList<String> out = new ArrayList<String>();
        if ((preferences = getSharedPreferences(pref, MODE_MULTI_PROCESS))!=null) {
            for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                Object obj = val.getKey();
                if (obj!=null) {
                    String tmp = obj.toString();
                    out.add(tmp);
                }
            }
        }
        return out;
    }

    private String getValue(String pref, String key){
        String out=null;
        if ((preferences = getSharedPreferences(pref, MODE_MULTI_PROCESS))!=null) {
        out = preferences.getString(key, key);
        }
        return out;
    }
}
