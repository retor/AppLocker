package com.retor.AppLocker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import com.retor.AppLocker.activitys.BlockActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Антон on 19.05.2014.
 */
public class ListenService extends Service {

    static final String testapp = "com.android.contacts";
    private static String SERVICE_ACTION = "com.retor.applocker.home";
    private String arguments;
    private SharedPreferences preferences;
    String[] apps = null;

    public void setArguments() {
        arguments = "logcat ActivityManager *:S";
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link android.content.Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     * <p/>
     * <p>For backwards compatibility, the default implementation calls
     * {@link #onStart} and returns either {@link #START_STICKY}
     * or {@link #START_STICKY_COMPATIBILITY}.
     * <p/>
     * <p>If you need your application to run on platform versions prior to API
     * level 5, you can use the following model to handle the older {@link #onStart}
     * callback in that case.  The <code>handleCommand</code> method is implemented by
     * you as appropriate:
     * <p/>
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java
     * start_compatibility}
     *
     * @param intent  The Intent supplied to {@link android.content.Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.  Currently either
     *                0, {@link #START_FLAG_REDELIVERY}, or {@link #START_FLAG_RETRY}.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "Created");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ListenService", "Closed");
        startService(new Intent(this, ListenService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apps = openPref(this);
        Thread thread = new Thread(new Checking());
        thread.setDaemon(true);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link android.os.IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p/>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link android.content.Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendBroadcastToReceiver() {
        Intent tosend = new Intent();
        tosend.setAction("com.retor.APP_FINDED");
        sendBroadcast(tosend);
    }


    public class Checking implements Runnable {
        @Override
        public void run() {
            setArguments();
            try {
                Runtime.getRuntime().exec("logcat -c");
                Process process = Runtime.getRuntime().exec(arguments);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while (reader.readLine() != null) {
                    String tmp = reader.readLine();
                    Log.d("CheckApps", tmp);
                    if (checkParam(tmp)){
                    //if ((tmp!=null && (tmp.contains("START") || tmp.contains("Displayed")))  && tmp.contains("act=android.intent.action.MAIN")){
                        for (String st:apps){
                            if (tmp.contains(st)) {
                                sendBroadcastToReceiver();
                                startBlockActivity(st);
                                Log.d("FindingApp", "App Finded!!!");
                            }
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void startBlockActivity(String appname){
        Intent block = new Intent(this, BlockActivity.class);
        block.putExtra("appname", appname);
        block.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(block);
    }

    private boolean checkParam(String in){
        if (in!=null){
            if (in.contains("START") &  in.contains("act=android.intent.action.MAIN")){
                return true;
            }
            if (in.contains("Displayed")){
                return true;
            }
        }
        return false;
    }

    private String[] openPref(Context context){
        preferences = context.getSharedPreferences("applock", 0);
        String[] out = null;
        Map<String, ?> map = preferences.getAll();
        if (map!=null) {
            out =  preferences. map.values().toArray();
            Log.d("openPref", out[0].toString());
        }
        return out;
    }
}
