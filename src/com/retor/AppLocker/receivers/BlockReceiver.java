package com.retor.AppLocker.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.retor.AppLocker.activitys.BlockActivity;
import com.retor.AppLocker.services.ListenService;

import java.util.ArrayList;

/**
 * Created by Антон on 20.05.2014.
 */
public class BlockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Received Action", intent.getAction() + " " + intent.getStringExtra("appname"));
        String action = intent.getAction();
        Intent serviceIntent = new Intent(context, ListenService.class);
        if (action != null && (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(BlockActivity.NORMAL))){
            String tmp=null;
            if(intent.hasExtra("appname")) {
                tmp = intent.getStringExtra("appname");
            }
            if (!isServiceRunning(context)) {
                context.stopService(serviceIntent);
                if(tmp!=null){
                   startBlockService(tmp, context);
                   Log.d("Receiver", "ReRun With");
                }else{
                   startBlockService(null, context);
                   Log.d("Receiver", "ReRun Without");
                }
            } else {
                context.getSharedPreferences("appsunlock", Context.MODE_MULTI_PROCESS).edit().clear().commit();
                if(tmp!=null){
                    startBlockService(tmp, context);
                    Log.d("Receiver", "NewRun With");
                }else{
                    startBlockService(null, context);
                    Log.d("Receiver", "NewRun Without");
                }
            }
        }

        if (action!=null && action.equals(BlockActivity.BLOCK)){
            startBlockActivity(intent.getStringExtra("appname"), context);
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

    private void startBlockActivity(String appname, Context cont){
        Intent block = new Intent(cont, BlockActivity.class);
        block.putExtra("appname", appname);
        block.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cont.startActivity(block);
    }

    private void startBlockService(String appname, Context cont){
        Intent service = new Intent(cont, ListenService.class);
        if (appname!=null){
            service.putExtra("appname", appname);
        }
        cont.startService(service);
    }
}
