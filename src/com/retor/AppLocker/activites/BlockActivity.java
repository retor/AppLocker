package com.retor.AppLocker.activites;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Cons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Антон on 30.05.2014.
 */
public class BlockActivity extends Activity {

    public static final String NORMAL = "startServiceNormal";
    protected boolean BAD_OFF = false;
    protected String app;
    private ActivityManager am;
    private PackageManager pm;
    private ImageView ico;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blockactivity);
        getActionBar().hide();
        setTheme(R.style.Theme_Base_Light);
        SharedPreferences preferences = getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS);
        password = preferences.getString("pass", null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText pass = (EditText)findViewById(R.id.passOnBlock);
        ImageButton forgot = (ImageButton)findViewById(R.id.forgotButton);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        pm = getPackageManager();
        app = getIntent().getStringExtra("appname");
        ico = (ImageView) findViewById(R.id.icoapp);
        final TextView apptitle = (TextView) findViewById(R.id.appBlock);
        int appUid = android.os.Process.myUid();
        int killUid = 0;
        try {
            Log.d("Blocker", app);
            ico.setImageDrawable(pm.getApplicationIcon(app));
            apptitle.setText(pm.getApplicationInfo(app, PackageManager.GET_META_DATA).loadLabel(pm));
            int killPid = 0;
            killUid = getPackageManager().getApplicationInfo(app, PackageManager.GET_META_DATA).uid;
            for (ActivityManager.RunningAppProcessInfo appi : am.getRunningAppProcesses()) {
                if (appi.uid == killUid)
                    killPid = appi.pid;
            }
            if (killUid != appUid) {
                android.os.Process.sendSignal(killUid, Process.SIGNAL_KILL);
                android.os.Process.killProcess(killPid);
            }
            am.killBackgroundProcesses(app);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Button unlock = (Button) findViewById(R.id.buttonUnlock);
        BAD_OFF = true;
        Log.d("App IN", getIntent().getStringExtra("appname"));
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Send extra", app);
                if ((pass.getText().toString()!=null) && pass.getText().toString().equals(password)) {
                    BAD_OFF = false;
                    getSharedPreferences("appsunlock", MODE_MULTI_PROCESS).edit().putString(app, getPackageManager().getLaunchIntentForPackage(app).getComponent().getClassName()).commit();
                    //startActivity(new Intent(getPackageManager().getLaunchIntentForPackage(app)));
                    //finish();
                    List<String> list = new ArrayList<String>();
                    if (!am.getRunningTasks(Integer.MAX_VALUE).contains(app))
                        startActivity(new Intent(getPackageManager().getLaunchIntentForPackage(app))
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET));
                    finish();
                }else{
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_top);
                    pass.setAnimation(anim);
                    pass.animate();
                    pass.setText("");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (BAD_OFF) {
            Log.d("BlockActivityKilling", app);
            am.killBackgroundProcesses(app);
        }

        /*else{
            List<String> list = new ArrayList<String>();
            for (ActivityManager.RunningAppProcessInfo app:am.getRunningAppProcesses()){
                list.add(app.processName);
            }
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(268435456);
            intent.setPackage(app);
            //getPackageManager().resolveActivity(intent, ApplicationInfo.FLAG_KILL_AFTER_RESTORE).activityInfo;
            if (!list.contains(app))
            startActivity(intent);
            //startActivity(new Intent(getPackageManager().getLaunchIntentForPackage(app)).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP));
        }*/
        //startService(new Intent(getApplicationContext(), ListenService.class));
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
/*        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_APP_SWITCH:
                break;
        }
        return true;*/
        return false;
    }

    private String getValue(String key) {
        SharedPreferences preferences;
        String out = null;
        if ((preferences = getSharedPreferences("applock", MODE_MULTI_PROCESS)) != null) {
            out = preferences.getString(key, key);
        }
        return out;
    }
}
