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

    ScheduledExecutorService executor;
    private SharedPreferences preferences;
    ArrayList<String> apps;
    ArrayList<String> appsNotBlock = new ArrayList<String>();
    Checking myTask;

    String TAG="ListenService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null && intent.getStringExtra("appname")!=null){
            Log.d(TAG, intent.toString());
            String t = intent.getStringExtra("appname");
            appsNotBlock.add(t);
            Log.d(TAG, "BLYA");
            myTask.addNot(appsNotBlock);
            //myTask = new Checking(appsNotBlock);
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
        apps = openPref();
        myTask = new Checking();
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(myTask, 70, 100, TimeUnit.MILLISECONDS);
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
            appsCheck = openPref();
        }

        public void addNot(ArrayList<String> in){
            appsNot = in;
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
                    if ((getRunningActivites().contains(appC) && (!checkRunningApps(appC))) && am.getRunningTasks(1).get(0).topActivity.getClassName().contains(appC)) {
                        Log.d(TAG, "AppFinded: " + appC);
                        sendBroadcast(new Intent(BlockActivity.BLOCK).putExtra("appname", appC));
                    }
                }
            }
            if (appsNot!=null) {
                for (String app : appsNot) {
                    if (!getRunningActivites().contains(app)) {
                        Log.d(TAG, "remove app: " + app);
                        appsNot.remove(app);
                    }
                }
            }
        }

        private boolean checkRunningApps(String appCheck) {
            if (appsNot != null) {
                if (appsNot.contains(appCheck)) {
                    Log.d(TAG, "app here: " + appCheck);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void run() {
            Log.d(TAG, "Zapuskaem zadachu");
            fullCheck();
        }
    }

    private ArrayList<String> openPref(){
        ArrayList<String> out = new ArrayList<String>();
        if ((preferences = getSharedPreferences("applock", MODE_MULTI_PROCESS))!=null) {
            for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                Object obj = val.getValue();
                if (obj!=null) {
                    String tmp = obj.toString();
                    out.add(tmp);
                }
            }
        }
        return out;
    }
}
