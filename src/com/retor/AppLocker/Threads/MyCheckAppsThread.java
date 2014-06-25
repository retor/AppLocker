package com.retor.AppLocker.Threads;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.retor.AppLocker.activites.BlockActivity;
import com.retor.AppLocker.classes.Cons;
import com.retor.AppLocker.interfaces.prefInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.ActivityManager.RunningTaskInfo;

/**
 * Created by Антон on 17.06.2014.
 */
public class MyCheckAppsThread implements prefInterface, Runnable {
    private SharedPreferences preferences;
    private Context context;
    private ArrayList<String> blocked;
    private ArrayList<String> worked;
    private ActivityManager am;
    private String BLOCKED;
    private String WORKED;
    private Intent blockin;

    public MyCheckAppsThread(Context _context, String prefNameBlock, String prefNameWork) {
        context = _context;
        BLOCKED = prefNameBlock;
        WORKED = prefNameWork;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public MyCheckAppsThread(String threadName, Context _context, String prefNameBlock, String prefNameWork) {
        context = _context;
        BLOCKED = prefNameBlock;
        WORKED = prefNameWork;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public void run() {
        //Log.d("MyThread: ", "Started");
        fillArrays(BLOCKED, WORKED);
        checkRunning();
    }

    private void checkRunning() {
        if (blocked != null) {
            List<RunningTaskInfo> tasks = am.getRunningTasks(Integer.MAX_VALUE);
            for (String app : blocked) {
                //Log.d("MyThread: ", app);
                //Log.d("MyThread: ", getRunningTasks().get(0).topActivity.getClassName().toString());
                for (RunningTaskInfo task : tasks) {
                    if (task.topActivity.getPackageName().contains(app) && (!checkUnBlockedArray(app)) && am.getRunningTasks(1).get(0).topActivity.getPackageName().contains(app)) {
                        Log.d("MyThread: ", "I Find It: " + app);
                        //am.killBackgroundProcesses(app);
                        context.startActivity(new Intent(context, BlockActivity.class).setFlags(PendingIntent.FLAG_CANCEL_CURRENT).putExtra(Cons.APPS_NAME, app).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            }
        }
    }

    private boolean checkUnBlockedArray(String appChecked) {
        if (worked != null && worked.contains(appChecked)) {
            return true;
        }
        return false;
    }

    private ArrayList<ActivityManager.RunningTaskInfo> getRunningTasks() {
        //Log.d("MyThread: ", "getRunningTasks");
        ArrayList<ActivityManager.RunningTaskInfo> tasks = new ArrayList<ActivityManager.RunningTaskInfo>();
        if (am != null)
            for (RunningTaskInfo task : am.getRunningTasks(Integer.MAX_VALUE)) {
                tasks.add(task);
            }
        return tasks;
    }

    private void fillArrays(String block, String work) {
        //Log.d("MyThread: ", "FillArrays");
        BLOCKED = block;
        if ((preferences = context.getSharedPreferences(block, Context.MODE_MULTI_PROCESS)) != null) {
            blocked = new ArrayList<String>();
            for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                Object obj = val.getKey();
                if (obj != null) {
                    String tmp = obj.toString();
                    blocked.add(tmp);
                }
            }
        }
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
    }

    @Override
    public String getValue(String pref, String key) {
        String out = null;
        if ((preferences = context.getSharedPreferences(pref, Context.MODE_MULTI_PROCESS)) != null) {
            out = preferences.getString(key, key);
        }
        return out;
    }
}
