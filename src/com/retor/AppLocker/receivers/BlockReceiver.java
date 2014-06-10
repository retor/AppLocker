package com.retor.AppLocker.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
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
            if (isServiceRunning(context)) {
                String tmp;
                if(intent.hasExtra("appname")) {
                    tmp = intent.getStringExtra("appname");
                    startBlockService(tmp, context);
                }else {
                    startBlockService(null, context);
                }
                //startBlockService(intent.getStringExtra("appname"), context);
                Log.d("Receiver", "Get extra " + intent.getStringExtra("appname"));
            } else {
                //context.stopService(serviceIntent);
                String tmp;
                if(intent.hasExtra("appname")) {
                    tmp = intent.getStringExtra("appname");
                    startBlockService(tmp, context);
                }else {
                    startBlockService(null, context);
                }
                Log.d("Receiver", "Get extra" + intent.getStringExtra("appname"));
                Toast.makeText(context, "ReRunning", Toast.LENGTH_SHORT).show();
            }
        }

        if (action!=null && action.equals(BlockActivity.BLOCK)){
            startBlockActivity(intent.getStringExtra("appname"), context);
        }
    }

    public boolean isServiceRunning(Context context) {
        Boolean returning = false;
        Log.d("isServiceRun", ListenService.class.getName().toString());
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> running = (ArrayList) manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : running) {
            if (service.service.getClassName().equals(ListenService.class.getName().toString())) {
                returning = true;
            } else {
                returning = false;
            }
        }
        return returning;
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
