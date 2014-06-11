package com.retor.AppLocker.activitys;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.retor.AppLocker.R;

/**
 * Created by Антон on 30.05.2014.
 */
public class BlockActivity extends Activity  {

    public static final String NORMAL = "startServiceNormal";
    public static final String BLOCK = "startServiceBlock";
    protected boolean BAD_OFF = false;
    protected String app;
    private ActivityManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blockactivity);
        am =(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        am.killBackgroundProcesses(app);
        Button unlock = (Button)findViewById(R.id.buttonUnlock);
        final TextView apptitle = (TextView)findViewById(R.id.appBlock);
        app = getIntent().getStringExtra("appname");
        apptitle.setText(app);
        BAD_OFF = true;
        Log.d("App IN", getIntent().getStringExtra("appname"));
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Send extra", apptitle.getText().toString());
                BAD_OFF = false;
                getSharedPreferences("appsunlock", MODE_MULTI_PROCESS).edit().putString(app, getPackageManager().getLaunchIntentForPackage(app).getComponent().getClassName()).commit();
                sendBroadcast(new Intent(NORMAL).putExtra("appname", app));
                //startActivity(new Intent(getPackageManager().getLaunchIntentForPackage(app)));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(BAD_OFF){
            Log.d("BlockActivityKilling", app);
            am.killBackgroundProcesses(app);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_APP_SWITCH:
                break;
        }
        return true;
    }

    private String getValue(String key){
        SharedPreferences preferences;
        String out=null;
        if ((preferences = getSharedPreferences("applock", MODE_MULTI_PROCESS))!=null) {
            out = preferences.getString(key, key);
        }
        return out;
    }
}
