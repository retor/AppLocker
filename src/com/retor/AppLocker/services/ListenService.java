package com.retor.AppLocker.services;

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
    MyCheckAppsThread myTaskBlock;
    MySessionMakerThread myTaskSession;

    String TAG = "ListenService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
        Log.d(TAG, "Closed");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
/*        PendingIntent pending = PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), ListenService.class),0);
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.setRepeating(0, 0, 6000, pending);*/
        myTaskBlock = new MyCheckAppsThread(getApplicationContext(), ALL, UNLOCK);
        myTaskSession = new MySessionMakerThread(getApplicationContext(), UNLOCK);
        myTaskBlock.setPriority(Thread.MAX_PRIORITY);
        myTaskBlock.setName("AppsChecker");
        myTaskBlock.setDaemon(true);
        myTaskSession.setPriority(Thread.MAX_PRIORITY);
        myTaskSession.setName("AppsSessions");
        myTaskSession.setDaemon(true);
        executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(myTaskBlock, 30, 150, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(myTaskSession, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
