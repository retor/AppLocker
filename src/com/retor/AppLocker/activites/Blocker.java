package com.retor.AppLocker.activites;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Cons;

/**
 * Created by Антон on 02.07.2014.
 */
public class Blocker extends Activity implements View.OnClickListener {
    protected boolean BAD_OFF = false;
    ActivityManager am;
    PackageManager pm;
    //Header
    private TextView appName;
    private ImageView appIcon;
    //Editable
    private TextView hint;
    private EditText editor;
    //keyboard
    private Button one_btn;
    private Button two_btn;
    private Button three_btn;
    private Button four_btn;
    private Button five_btn;
    private Button six_btn;
    private Button seven_btn;
    private Button eight_btn;
    private Button nine_btn;
    private Button zero_btn;
    private Button back_btn;
    private Button forgot_btn;
    private int MODE;
    private String savedPassword;
    private String savedWord;
    private Animation anim;
    private String app;
    private InputMethodManager imm;
    public Blocker() {
    }

    private void initial() {
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_top);
        getActionBar().hide();
        if (getIntent().getIntExtra("mode", 0) != 0) {
            MODE = getIntent().getIntExtra("mode", 0);
            Log.d("Blocker", String.valueOf(MODE));
        }
        if (getIntent().getStringExtra(Cons.APPS_NAME)!=null){
            am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            pm = getPackageManager();
            int appUid = android.os.Process.myUid();
            int killUid = 0;
            try {
                int killPid = 0;
                killUid = getPackageManager().getApplicationInfo(getIntent().getStringExtra(Cons.APPS_NAME), PackageManager.GET_META_DATA).uid;
                for (ActivityManager.RunningAppProcessInfo appi : am.getRunningAppProcesses()) {
                    if (appi.uid == killUid)
                        killPid = appi.pid;
                }
                if (killUid != appUid) {
                    android.os.Process.sendSignal(killUid, android.os.Process.SIGNAL_KILL);
                    android.os.Process.killProcess(killPid);
                }
                am.killBackgroundProcesses(app);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        BAD_OFF = true;
        if (getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS) != null && getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).getBoolean(Cons.APP_PREF_PASS_SET, false)) {
            savedPassword = getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).getString(Cons.APP_PREF_PASS, null);
            savedWord = getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).getString(Cons.APP_PREF_WORD, null);
        }
        //Header
        appName = (TextView) findViewById(R.id.pa_appname);
        appName.setAnimation(anim);
        appName.animate();
        appIcon = (ImageView) findViewById(R.id.pa_appico);
        appIcon.setAnimation(anim);
        appIcon.animate();
        //Editor
        hint = (TextView) findViewById(R.id.pa_hint);
        hint.setAnimation(anim);
        hint.animate();
        editor = (EditText) findViewById(R.id.pa_editor);
        editor.setAnimation(anim);
        editor.animate();
        //Keyboard
        one_btn = (Button) findViewById(R.id.one_btn);
        one_btn.setOnClickListener(this);
        two_btn = (Button) findViewById(R.id.two_btn);
        two_btn.setOnClickListener(this);
        three_btn = (Button) findViewById(R.id.three_btn);
        three_btn.setOnClickListener(this);
        four_btn = (Button) findViewById(R.id.four_btn);
        four_btn.setOnClickListener(this);
        five_btn = (Button) findViewById(R.id.five_btn);
        five_btn.setOnClickListener(this);
        six_btn = (Button) findViewById(R.id.six_btn);
        six_btn.setOnClickListener(this);
        seven_btn = (Button) findViewById(R.id.seven_btn);
        seven_btn.setOnClickListener(this);
        eight_btn = (Button) findViewById(R.id.eight_btn);
        eight_btn.setOnClickListener(this);
        nine_btn = (Button) findViewById(R.id.nine_btn);
        nine_btn.setOnClickListener(this);
        zero_btn = (Button) findViewById(R.id.zero_btn);
        zero_btn.setOnClickListener(this);
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder(editor.getText().toString());
                int currentLength = sb.length();
                if (sb.length() > 0) {
                    currentLength--;
                    sb.deleteCharAt(currentLength);
                    editor.setText(sb);
                }
            }
        });
        forgot_btn = (Button) findViewById(R.id.forgot_btn);
        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Blocker.class);
                intent.putExtra("mode", Cons.MODE_FORGOT).putExtra("modeold", getIntent().getIntExtra("mode", 0));
                if (getIntent().getStringExtra(Cons.APPS_NAME) != null) {
                    intent.putExtra(Cons.APPS_NAME, getIntent().getStringExtra(Cons.APPS_NAME));
                }
                startActivity(intent);
            }
        });
        if (getIntent().getStringExtra(Cons.APPS_NAME) != null) {
            app = getIntent().getStringExtra(Cons.APPS_NAME);
            am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            pm = getPackageManager();
            try {
                appIcon.setImageDrawable(pm.getApplicationIcon(app));
                appName.setText(pm.getApplicationInfo(app, PackageManager.GET_META_DATA).loadLabel(pm));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        startActivity(intent);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordactivity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        initial();
        if (getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS) != null && !getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).getBoolean(Cons.APP_PREF_PASS_SET, false)) {
            MODE = Cons.MODE_FIRST_RUN;
        }
        if (getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).getBoolean(Cons.APP_PREF_PASS_SET, false) && MODE==0){
            MODE = Cons.MODE_AUTH_MY;
        }

        if (MODE == Cons.MODE_FIRST_RUN) {
            appName.setText("Создание пароля");
            appName.animate();
            appIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
            appIcon.animate();
            passwordCreate();
            BAD_OFF=false;
        }

        if (MODE == Cons.MODE_AUTH_MY){
            appName.setText("Авторизация для Входа");
            appName.animate();
            appIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
            appIcon.animate();
            hint.setText("Input password");
            hint.animate();
            editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            imm.hideSoftInputFromWindow(editor.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            editor.animate();
            hint.animate();
            editor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String input = editor.getText().toString();
                    if (savedPassword.length()==input.length()){
                        if (savedPassword.equals(input)){
                            finish();
                            startMain();
                        }else{
                            editor.setText("");
                            editor.animate();
                        }
                    }
                }
            });
            BAD_OFF=false;
        }
        if (MODE==Cons.MODE_AUTH_APP) {
            hint.setText("Input password");
            hint.animate();
            editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editor.animate();
            hint.animate();
            int appUid = android.os.Process.myUid();
            int killUid = 0;
            try {
                Log.d("Blocker", app);
                appIcon.setImageDrawable(pm.getApplicationIcon(app));
                appName.setText(pm.getApplicationInfo(app, PackageManager.GET_META_DATA).loadLabel(pm));
                int killPid = 0;
                killUid = getPackageManager().getApplicationInfo(app, PackageManager.GET_META_DATA).uid;
                for (ActivityManager.RunningAppProcessInfo appi : am.getRunningAppProcesses()) {
                    if (appi.uid == killUid)
                        killPid = appi.pid;
                }
                if (killUid != appUid) {
                    android.os.Process.sendSignal(killUid, android.os.Process.SIGNAL_KILL);
                    android.os.Process.killProcess(killPid);
                }
                am.killBackgroundProcesses(app);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            BAD_OFF = true;
            Log.d("App IN", getIntent().getStringExtra(Cons.APPS_NAME));
            editor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    String input = editor.getText().toString();
                    if (input.length() == savedPassword.length()) {
                        if (savedPassword.equals(input)) {
                            BAD_OFF = false;
                            getSharedPreferences(Cons.APPS_UNLOCK, MODE_MULTI_PROCESS).edit().putString(app, getPackageManager().getLaunchIntentForPackage(app).getComponent().getClassName()).commit();
                            addTimer(app);
                            if (!am.getRunningTasks(Integer.MAX_VALUE).contains(app)) {
                                startActivity(new Intent(Intent.ACTION_MAIN).setComponent(getPackageManager().getLaunchIntentForPackage(app).getComponent())
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY));
                            }
                            BAD_OFF=false;
                            finish();
                        } else {
                            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_top);
                            editor.setAnimation(anim);
                            editor.animate();
                            editor.setText("");
                        }
                    }
                }
            });
            BAD_OFF=false;
        }
        if (MODE==Cons.MODE_FORGOT){
            hideKeyboard();
            appName.setText("Восстановление пароля");
            appName.animate();
            appIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
            appIcon.animate();
            hint.setText("Input word");
            hint.animate();
            editor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    editor.onTouchEvent(event); // call native handler
                    editor.setInputType(InputType.TYPE_CLASS_TEXT); // restore input type
                    return true; // consume touch even
                }
            });
            editor.setText("");
            editor.setInputType(InputType.TYPE_CLASS_TEXT);
            editor.animate();
            hint.animate();
            imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);//editor.requestFocus();
            editor.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN) &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 || event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                        if (checkStrings(savedWord, editor.getText().toString())) {
                            editor.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    int inType = editor.getInputType(); // backup the input type
                                    editor.setInputType(InputType.TYPE_NULL); // disable soft input
                                    editor.onTouchEvent(event); // call native handler
                                    editor.setInputType(inType); // restore input type
                                    return true; // consume touch even
                                }
                            });
                            imm.setCurrentInputMethodSubtype(null);
                            passwordCreate();
                        } else {
                            editor.animate();
                            hint.animate();
                            editor.setText("");
                        }
                    }
                    return true;
                }
            });
            BAD_OFF=false;
        }
        if (MODE==Cons.MODE_NEW_PASS){
            hideKeyboard();
            appName.setText("Изменение пароля");
            appName.animate();
            appIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
            appIcon.animate();
            hint.setText("Input word");
            hint.animate();
            editor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    editor.onTouchEvent(event); // call native handler
                    editor.setInputType(InputType.TYPE_CLASS_TEXT); // restore input type
                    return true; // consume touch even
                }
            });
            editor.setText("");
            editor.setInputType(InputType.TYPE_CLASS_TEXT);
            editor.animate();
            hint.animate();
            imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);//editor.requestFocus();
            editor.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN) &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 || event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                        if (checkStrings(savedWord, editor.getText().toString())) {
                            editor.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    int inType = editor.getInputType(); // backup the input type
                                    editor.setInputType(InputType.TYPE_NULL); // disable soft input
                                    editor.onTouchEvent(event); // call native handler
                                    editor.setInputType(inType); // restore input type
                                    return true; // consume touch even
                                }
                            });
                            imm.showSoftInput(editor, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            imm.hideSoftInputFromInputMethod(editor.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                            passwordCreate();
                        } else {
                            editor.animate();
                            hint.animate();
                            editor.setText("");
                        }
                    }
                    return true;
                }
            });
            BAD_OFF=false;
        }
        if (MODE == Cons.MODE_NEW_WORD){
            appName.setText("Изменение слова");
            appName.animate();
            appIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
            appIcon.animate();
            editor.setText("");
            showKeyboard();
            hint.setText("Input password");
            hint.animate();
            editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editor.animate();
            hint.animate();
            editor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = editor.getInputType(); // backup the input type
                    editor.setInputType(InputType.TYPE_NULL); // disable soft input
                    editor.onTouchEvent(event); // call native handler
                    editor.setInputType(inType); // restore input type
                    return true; // consume touch even
                }
            });
            editor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    final String input = editor.getText().toString();
                    if (input.length()==savedPassword.length()){
                        if (savedPassword.equals(input)){
                            editor.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    editor.onTouchEvent(event); // call native handler
                                    editor.setInputType(InputType.TYPE_CLASS_TEXT); // restore input type
                                    return true; // consume touch even
                                }
                            });
                            editor.removeTextChangedListener(this);
                            imm.setCurrentInputMethodSubtype(null);
                            wordCreate();
                        }else{
                            hint.animate();
                            editor.animate();
                            editor.setText("");
                        }
                    }
                }
            });
            BAD_OFF=false;
        }
    }

    private void passwordCreate() {
        editor.setText("");
        showKeyboard();
        hint.setText("Input password");
        hint.animate();
        editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        editor.animate();
        hint.animate();
        editor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = editor.getInputType(); // backup the input type
                editor.setInputType(InputType.TYPE_NULL); // disable soft input
                editor.onTouchEvent(event); // call native handler
                editor.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        });
        imm.setCurrentInputMethodSubtype(null);
        forgot_btn.setText("Done");
        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = editor.getText().toString();
                if (input.length() >= 4) {
                    hint.setText("Confirm password");
                    hint.animate();
                    editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    editor.setText("");
                    editor.animate();
                    hint.animate();
                    editor.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int inType = editor.getInputType(); // backup the input type
                            editor.setInputType(InputType.TYPE_NULL); // disable soft input
                            editor.onTouchEvent(event); // call native handler
                            editor.setInputType(inType); // restore input type
                            return true; // consume touch even
                        }
                    });
                    forgot_btn.setOnClickListener(null);
                    forgot_btn = (Button)findViewById(R.id.forgot_btn);
                    forgot_btn.setEnabled(true);
                    forgot_btn.setText("Done");
                    forgot_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (input.length() == editor.getText().toString().length()) {
                                if (checkStrings(input, editor.getText().toString())) {
                                    getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).edit()
                                            .putString(Cons.APP_PREF_PASS, input).commit();
                                    if (MODE == Cons.MODE_FIRST_RUN) {
                                        wordCreate();
                                    }
                                    if (MODE == Cons.MODE_FORGOT || MODE == Cons.MODE_NEW_PASS) {
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), Blocker.class);
                                        intent.putExtra("mode", (getIntent().getIntExtra("modeold",0)));
                                        if (getIntent().getStringExtra(Cons.APPS_NAME) != null) {
                                            intent.putExtra(Cons.APPS_NAME, getIntent().getStringExtra(Cons.APPS_NAME));
                                        }
                                        BAD_OFF=false;
                                        startActivity(intent);
                                    }
                                } else {
                                    hint.animate();
                                    editor.animate();
                                    editor.setText("");
                                }
                            } else {
                                hint.animate();
                                editor.animate();
                                editor.setText("");
                            }
                        }
                    });
                }else {
                    hint.animate();
                    editor.animate();
                    editor.setText("");
                }
            }
        });
    }

    private void wordCreate() {
        hideKeyboard();
        appName.setText("Создание слова");
        appName.animate();
        appIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
        appIcon.animate();
        hint.setText("Input word");
        hint.animate();
        editor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editor.onTouchEvent(event); // call native handler
                editor.setInputType(InputType.TYPE_CLASS_TEXT); // restore input type
                return true; // consume touch even
            }
        });
        imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);
        editor.setText("");
        editor.setInputType(InputType.TYPE_CLASS_TEXT);
        editor.animate();
        hint.animate();
        editor.requestFocus();
        editor.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN) &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 || event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                    final String word = editor.getText().toString();
                    if (word != null && word.length()>3) {
                        hint.setText("Confirm word");
                        hint.animate();
                        editor.setText("");
                        editor.setInputType(InputType.TYPE_CLASS_TEXT);
                        editor.animate();
                        hint.animate();
                        imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);
                        editor.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN) &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 || event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                                    if (checkStrings(word, editor.getText().toString())) {
                                        imm.setCurrentInputMethodSubtype(null);
                                        getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).edit()
                                                .putString(Cons.APP_PREF_WORD, word).commit();
                                        if (MODE == Cons.MODE_FIRST_RUN || MODE == Cons.MODE_NEW_WORD){
                                            getSharedPreferences(Cons.APP_PREF, MODE_MULTI_PROCESS).edit().putBoolean(Cons.APP_PREF_PASS_SET, true).commit();
                                            imm.setCurrentInputMethodSubtype(null);
                                            BAD_OFF=false;
                                            finish();
                                            startMain();
                                        }
                                    } else {
                                        editor.animate();
                                        hint.animate();
                                        editor.setText("");
                                    }
                                }
                                return true;
                            }
                        });
                    } else {
                        editor.animate();
                        hint.animate();
                        editor.setText("");
                    }

                }
                return true;
            }
        });
    }

    private boolean checkStrings(String input1, String input2) {
        if (input1.length() == input2.length()) {
            if (input1.equals(input2))
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        editor.append(((Button) v).getText().toString());
    }

    @Override
    protected void onDestroy() {
/*        if (BAD_OFF){
            if (MODE!=Cons.MODE_AUTH_MY && MODE!=Cons.MODE_FIRST_RUN && MODE!=Cons.MODE_FORGOT)
            am.killBackgroundProcesses(app);
        }*/
        if (BAD_OFF) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(268435456);
            startActivity(intent);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MODE!=Cons.MODE_AUTH_MY && MODE!=Cons.MODE_FIRST_RUN)
        finish();
    }

    private void startMain(){
        startActivity(new Intent(this, Home.class)
                .setFlags(PendingIntent.FLAG_CANCEL_CURRENT)
                .putExtra("mode", Cons.MODE_FIRST_RUN)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void hideKeyboard(){
        LinearLayout keyboard = (LinearLayout)findViewById(R.id.keyboard);
        keyboard.setVisibility(View.INVISIBLE);
    }
    private void showKeyboard(){
        LinearLayout keyboard = (LinearLayout)findViewById(R.id.keyboard);
        keyboard.setVisibility(View.VISIBLE);
    }

    private void addTimer(String app) {
        long time = (System.currentTimeMillis() + (60 * 1000));
        SharedPreferences preferences = getSharedPreferences(Cons.APP_TIMER, Context.MODE_MULTI_PROCESS);
        preferences.edit().putLong(app, time).commit();
    }
}
