package com.retor.AppLocker.Threads;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.retor.AppLocker.interfaces.prefInterface;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Антон on 19.06.2014.
 */
public class MySessionMakerThread extends Thread implements Runnable, prefInterface {

    final String TAG_LOG = "MySessionThread: ";
    private SharedPreferences preferences;
    private Context context;
    private ArrayList<String> worked;
    private ArrayList<String> timers;
    private ActivityManager am;
    private String WORKED;
    private String TIMERPREF = "appstimer";


    public MySessionMakerThread(Context _context, String WORKEDpref) {
        context = _context;
        WORKED = WORKEDpref;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public MySessionMakerThread(String threadName, Context _context, String WORKEDpref) {
        super(threadName);
        context = _context;
        WORKED = WORKEDpref;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public void run() {
        fillArray(WORKED, TIMERPREF);
        makeWorkedCheck();
        super.run();
    }

    private void makeWorkedCheck() {
        if (worked != null) {
            Log.d(TAG_LOG, "Started");
            for (String app : worked) {
                Log.d(TAG_LOG, app);
                if (checkRunning(app) && checkTop(app) && checkTimer(app)) {
                    Log.d(TAG_LOG, "if app have timer");
                    renevTimer(app);
                    return;
                }
                if (checkRunning(app) && checkTop(app) && !checkTimer(app)) {
                    Log.d(TAG_LOG, "if app dont have timer");
                    addTimer(app);
                    return;
                }
                if (checkRunning(app) && !checkTop(app) && checkTimer(app)) {
                    Log.d(TAG_LOG, "if app not top");
                    if (getTimerValue(TIMERPREF, app) >= System.currentTimeMillis()) {
                        delTimer(app);
                        delApp(app);
                        return;
                    }
                }
                if (checkRunning(app) && !checkTop(app) && !checkTimer(app)) {
                    Log.d(TAG_LOG, "if app without timer and top");
                    delApp(app);
                    return;
                }
                if (!checkRunning(app)) {
                    Log.d(TAG_LOG, "if app not running");
                    delApp(app);
                    if (checkTimer(app)) {
                        delTimer(app);
                    }
                    return;
                }
            }
        }
    }

    private boolean checkRunning(String appName) {
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningTaskInfo proc : am.getRunningTasks(Integer.MAX_VALUE)) {
            if (proc.topActivity.getPackageName().contains(appName)) return true;
        }
        return false;
    }

    private boolean checkTop(String appName) {
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningTaskInfo task : am.getRunningTasks(Integer.MAX_VALUE)) {
            if (task.topActivity.getPackageName().contains(appName) && am.getRunningTasks(1).get(0).topActivity.getPackageName().contains(appName))
                return true;
        }
        return false;
    }

    private boolean checkTimer(String appName) {
        if (timers != null) {
            for (String timer : timers) {
                if (timer.contains(appName)) return true;
            }
        }
        return false;
    }

    private void addTimer(String app) {
        long time = (System.currentTimeMillis() + ((60 * 1000) * 5));
        preferences = context.getSharedPreferences(TIMERPREF, Context.MODE_MULTI_PROCESS);
        preferences.edit().putLong(app, time).commit();
        fillArray(WORKED, TIMERPREF);
    }

    private void renevTimer(String app) {
        preferences = context.getSharedPreferences(TIMERPREF, Context.MODE_MULTI_PROCESS);
        if (preferences.contains(app)) {
            delTimer(app);
            addTimer(app);
        } else {
            addTimer(app);
        }
    }

    private void delTimer(String app) {
        preferences = context.getSharedPreferences(TIMERPREF, Context.MODE_MULTI_PROCESS);
        if (timers != null) {
            timers.remove(app);
        }
        if (preferences.contains(app)) {
            preferences.edit().remove(app).commit();
        }
        Log.d(TAG_LOG, "RemovePref " + app);
    }

    private void delApp(String app) {
        if (worked != null) {
            worked.remove(app);
            preferences = context.getSharedPreferences(WORKED, Context.MODE_MULTI_PROCESS);
            preferences.edit().remove(app).commit();
            Log.d(TAG_LOG, "Deleted " + app);
        }
    }

    private void fillArray(String work, String timer) {
        Log.d(TAG_LOG, "FillArrays");
        WORKED = work;
        if ((preferences = context.getSharedPreferences(work, Context.MODE_MULTI_PROCESS)) != null) {
            worked = new ArrayList<String>();
            for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                Object obj = val.getKey();
                if (obj != null) {
                    String tmp = obj.toString();
                    worked.add(tmp);
                }
            }
        }
        if ((preferences = context.getSharedPreferences(timer, Context.MODE_MULTI_PROCESS)) != null) {
            timers = new ArrayList<String>();
            for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                Object obj = val.getKey();
                if (obj != null) {
                    String tmp = obj.toString();
                    timers.add(tmp);
                }
            }
        }
    }

    @Override
    public String getValue(String pref, String key) {
        String out = null;
        if ((preferences = context.getSharedPreferences(pref, Context.MODE_MULTI_PROCESS)) != null) {
            out = preferences.getString(key, key);
        }
        return out;
    }

    public long getTimerValue(String pref, String key) {
        long out = 0;
        if ((preferences = context.getSharedPreferences(pref, Context.MODE_MULTI_PROCESS)) != null) {
            out = preferences.getLong(key, 0);
        }
        return out;
    }
}
