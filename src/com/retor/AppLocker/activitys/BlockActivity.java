package com.retor.AppLocker.activitys;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blockactivity);
        Button unlock = (Button)findViewById(R.id.buttonUnlock);
        final TextView apptitle = (TextView)findViewById(R.id.appBlock);
        apptitle.setText(getIntent().getStringExtra("appname"));
        BAD_OFF = true;
        Log.d("App IN", getIntent().getStringExtra("appname"));
        //app = workWithString(getIntent().getStringExtra("appname"));
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Send extra", apptitle.getText().toString());
                BAD_OFF = false;
                sendBroadcast(new Intent(NORMAL).putExtra("appname", apptitle.getText().toString()));
                finish();
            }
        });
    }

    private String workWithString(String in){
        String out;
        int pos = in.indexOf(".");
        pos = in.indexOf(".", pos+1);
        pos = in.indexOf(".", pos+1);
        out = in.substring(0,pos);
        return out;
    }

    @Override
    protected void onDestroy() {
        if(BAD_OFF){
            ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
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
}
