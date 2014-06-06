package com.retor.AppLocker.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import com.retor.AppLocker.activitys.BlockActivity;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras().containsKey("block")){
            executor = Executors.newScheduledThreadPool(1);
            //executor.schedule(new Checking(), 100, TimeUnit.MILLISECONDS);
            executor.scheduleAtFixedRate(new Checking(), 100, 1000, TimeUnit.MILLISECONDS);
        }else{
            Log.d("Service thread", "Stop");
        }

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
        public Checking(){
            appsCheck = openPref();
        }

        @Override
        public void run() {
            Log.d("Servis moi", "Zapuskaem zadachu");
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

            }
        }
    }

    private ArrayList<String> openPref(){
        ArrayList<String> out = new ArrayList<String>();
        if ((preferences = getSharedPreferences("applock", MODE_MULTI_PROCESS))!=null) {
                for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                    Object obj = val.getValue();
                    String tmp = obj.toString();
                    out.add(tmp);
            }
        }
//            Log.d("openPref", out.get(0).toString());
        return out;
    }

    private boolean checkAppinList(ArrayList<String> appsIn, String toCheck){
        for (String ch:appsIn){
            if (toCheck.contains(ch))
                return true;
        }
        return false;
    }
}
