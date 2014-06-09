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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra("appname")) {
            String tmp = intent.getStringExtra("appname");
            if (tmp != null) {
                appsNotBlock.add(tmp);
                executor.scheduleAtFixedRate(new Checking(), 100, 1000, TimeUnit.MILLISECONDS);
            }
        }
            //executor.schedule(new Checking(), 100, TimeUnit.MILLISECONDS);
            //Log.d("Service thread", "Stop");
        Log.d("Service", "Created");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ListenService", "Closed");
        //startService(new Intent(this, ListenService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apps = openPref();
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Checking(), 100, 1000, TimeUnit.MILLISECONDS);
//        PendingIntent pinnok = PendingIntent.getService(this, 0, new Intent(this, ListenService.class), 0);
//        AlarmManager alarmik = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmik.setRepeating(0, Calendar.getInstance().getTimeInMillis(), 1000, pinnok);
//      ExecutorService executor = Executors.newSingleThreadScheduledExecutor().schedule(new Checking(), 10, TimeUnit.SECONDS);// .newSingleThreadExecutor();
/*        Thread thread = new Thread(new Checking());
        thread.setDaemon(true);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();*/
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class Checking implements Runnable {

        ArrayList<String> appsCheck;
        Context context;

        public Checking(){
            context=getApplicationContext();
            appsCheck = openPref();
        }

        private void startBlockActivity(String appname, Context cont){
            Intent block = new Intent(cont, BlockActivity.class);
            block.putExtra("appname", appname);
            block.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cont.startActivity(block);
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
            for(String appC:appsCheck){
                if((getRunningActivites().contains(appC) && (!checkRunningApps(appC))) && am.getRunningTasks(1).get(0).topActivity.getClassName().contains(appC)){
                    Log.d("Service(AppFinder)", "AppFinded: "+ appC);
                    sendBroadcast(new Intent(BlockActivity.BLOCK).putExtra("appname", appC));
                }
            }
        }

        private boolean checkRunningApps(String appCheck){
            if (appsNotBlock!=null){
            ArrayList<String> running = getRunningActivites();
            for (String app:appsNotBlock){
                if (!running.contains(app)){
                    appsNotBlock.remove(app);
                }else{
                    if(appsNotBlock.contains(appCheck)){
                        return true;
                    }
                }
                }
            }
            return false;
        }

        @Override
        public void run() {
            Log.d("Servis moi", "Zapuskaem zadachu");
            fullCheck();
          /*
            ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
            String tm;
            List<String> lis = new ArrayList<String>();
            List<ActivityManager.RunningTaskInfo> get = new ArrayList<ActivityManager.RunningTaskInfo>(am.getRunningTasks(Integer.MAX_VALUE));
            for (ActivityManager.RunningTaskInfo app:get){
                if ((tm=app.topActivity.getClassName().toString())!=null)
                    Log.d("FindingApp", tm);
                lis.add(tm);
            }
            for (String s:lis){
                if ((checkAppinList(appsCheck, s))){
                    sendBroadcast(new Intent().setAction(BlockActivity.BLOCK));
                    executor.shutdown();
                    Log.d("FindingApp", "App Finded!!!");
                }

            }*/
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
