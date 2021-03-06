package com.retor.AppLocker.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.retor.AppLocker.classes.Cons;
import com.retor.AppLocker.services.ListenService;

import java.util.ArrayList;

/**
 * Created by Антон on 20.05.2014.
 */
public class BlockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = new Intent(context, ListenService.class);
        if (action != null && (action.equals(Intent.ACTION_BOOT_COMPLETED))) {
            context.startService(serviceIntent);
        }
    }

    public boolean isServiceRunning(Context context) {
        Log.d("isServiceRun", ListenService.class.getName().toString());
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> running = (ArrayList) manager.getRunningServices(Integer.MAX_VALUE);
        Log.d("isServiceRun", running.get(0).service.getClassName());
        for (ActivityManager.RunningServiceInfo service : running) {
            if (service.service.getClassName().contains(ListenService.class.getName().toString())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void startBlockService(String appname, Context cont) {
        Intent service = new Intent(cont, ListenService.class);
        if (appname != null) {
            service.putExtra(Cons.APPS_NAME, appname);
        }
        cont.startService(service);
    }
}
