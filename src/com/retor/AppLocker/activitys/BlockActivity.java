package com.retor.AppLocker.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.retor.AppLocker.R;
import com.retor.AppLocker.services.ListenService;

/**
 * Created by Антон on 30.05.2014.
 */
public class BlockActivity extends Activity  {

    public static final String NORMAL = "startServiceNormal";
    public static final String BLOCK = "startServiceBlock";

    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blockactivity);
        serviceIntent = new Intent(getApplicationContext(), ListenService.class);
        Button unlock = (Button)findViewById(R.id.buttonUnlock);
        final TextView apptitle = (TextView)findViewById(R.id.appBlock);
        apptitle.setText(getIntent().getStringExtra("appname"));
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(BlockActivity.NORMAL).putExtra("appname", apptitle.getText()));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
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
