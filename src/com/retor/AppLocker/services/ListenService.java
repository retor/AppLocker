package com.retor.AppLocker.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.retor.AppLocker.Threads.MyCheckAppsThread;
import com.retor.AppLocker.Threads.MySessionMakerThread;

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
    ScheduledExecutorService executor1;
    MyCheckAppsThread myTaskBlock;
    MySessionMakerThread myTaskSession;

    String TAG = "ListenService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
        Log.d(TAG, "Closed");
        startService(new Intent(getApplicationContext(), ListenService.class));
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
/*        myTaskBlock = new MyCheckAppsThread(getApplicationContext(), ALL, UNLOCK);
        myTaskSession = new MySessionMakerThread(getApplicationContext(), UNLOCK);
        myTaskBlock.setPriority(Thread.MAX_PRIORITY);
        myTaskBlock.setName("AppsChecker");
        myTaskBlock.setDaemon(true);
        myTaskSession.setPriority(Thread.MAX_PRIORITY);
        myTaskSession.setName("AppsSessions");
        myTaskSession.setDaemon(true);*/
        executor = Executors.newScheduledThreadPool(1);
        executor1 = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new MyCheckAppsThread(getApplicationContext(), ALL, UNLOCK), 5, 50, TimeUnit.MILLISECONDS);
        executor1.scheduleAtFixedRate(new MySessionMakerThread(getApplicationContext(), UNLOCK), 0, 35, TimeUnit.SECONDS);
        PendingIntent pending = PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), ListenService.class),0);
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.setRepeating(0, 0, 6000, pending);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
