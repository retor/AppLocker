package com.retor.AppLocker.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.retor.AppLocker.services.ListenService;

import java.util.ArrayList;

/**
 * Created by Антон on 20.05.2014.
 */
public class BlockReceiver extends BroadcastReceiver {

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.  During this time you can use the other methods on
     * BroadcastReceiver to view/modify the current result values.  This method
     * is always called within the main thread of its process, unless you
     * explicitly asked for it to be scheduled on a different thread using
     * {@link android.content.Context#registerReceiver(android.content.BroadcastReceiver,
     * IntentFilter, String, android.os.Handler)}. When it runs on the main
     * thread you should
     * never perform long-running operations in it (there is a timeout of
     * 10 seconds that the system allows before considering the receiver to
     * be blocked and a candidate to be killed). You cannot launch a popup dialog
     * in your implementation of onReceive().
     * <p/>
     * <p><b>If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
     * then the object is no longer alive after returning from this
     * function.</b>  This means you should not perform any operations that
     * return a result to you asynchronously -- in particular, for interacting
     * with services, you should use
     * {@link android.content.Context#startService(android.content.Intent)} instead of
     * {@link android.content.Context#bindService(android.content.Intent, ServiceConnection, int)}.  If you wish
     * to interact with a service that is already running, you can use
     * {@link #peekService}.
     * <p/>
     * <p>The Intent filters used in {@link android.content.Context#registerReceiver}
     * and in application manifests are <em>not</em> guaranteed to be exclusive. They
     * are hints to the operating system about how to find suitable recipients. It is
     * possible for senders to force delivery to specific recipients, bypassing filter
     * resolution.  For this reason, {@link #onReceive(android.content.Context, android.content.Intent) onReceive()}
     * implementations should respond only to known actions, ignoring any unexpected
     * Intents that they may receive.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", intent.getAction());
        String action = intent.getAction();
            if (action!=null && action.equals(Intent.ACTION_BOOT_COMPLETED) | action.equals(Intent.ACTION_DREAMING_STOPPED)){
                Intent serviceIntent = new Intent(context, ListenService.class);
                if (isServiceRunning(context)){
                    context.startService(serviceIntent);
                }else {
                    Toast.makeText(context, "Running", Toast.LENGTH_SHORT).show();
                }
            }
    }

    public  boolean isServiceRunning(Context context){
        Boolean returning = null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> running = (ArrayList)manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service: running){
            if (service.service.getClassName().equals(ListenService.class.getName())){
                returning =  true;
            }else{
                returning =  false;
            }
        }

        return returning;
    }

    class MyTask implements Runnable {

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {

        }
    }

}
