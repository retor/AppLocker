package com.retor.AppLocker.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import com.retor.AppLocker.classes.Cons;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Антон on 25.06.2014.
 */
public class NotificationListener extends AccessibilityService {
    boolean isInit = false;
    ArrayList<String> blocked;
    /**
     * Callback for {@link android.view.accessibility.AccessibilityEvent}s.
     *
     * @param event An event.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        fillArray();
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            Toast.makeText(getApplicationContext(), event.getPackageName().toString(), Toast.LENGTH_SHORT).show();
            if (blocked!=null && event.getPackageName()!=null){
                for (String app:blocked){
                    if (app.contains(event.getPackageName().toString())){
                        event.setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        isInit = false;
    }

    @Override
    public void onServiceConnected(){
        Log.d("Notifi Service", "Service connected");
        if (isInit) {
            return;
        }
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        setServiceInfo(info);
        isInit = true;
    }


    private void fillArray() {
        //Log.d("MyThread: ", "FillArrays");
        SharedPreferences preferences;
        if ((preferences = getSharedPreferences(Cons.APPS_LOCK, Context.MODE_MULTI_PROCESS)) != null) {
            blocked = new ArrayList<String>();
            for (Map.Entry<?, ?> val : preferences.getAll().entrySet()) {
                Object obj = val.getKey();
                if (obj != null) {
                    String tmp = obj.toString();
                    blocked.add(tmp);
                }
            }
        }
    }
}
