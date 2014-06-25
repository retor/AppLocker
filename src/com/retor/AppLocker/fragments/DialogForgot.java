package com.retor.AppLocker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.retor.AppLocker.R;
import com.retor.AppLocker.activites.BlockActivity;
import com.retor.AppLocker.classes.Cons;

/**
 * Created by Антон on 24.06.2014.
 */
public class DialogForgot extends DialogFragment {
    Context context;
    TextView anshint;
    TextView old;
    TextView newpas;
    TextView confirm;
    EditText oldpassword;
    EditText newpassword;
    EditText confirmpassword;
    EditText answer;
    private int MODE;
    //MODE 1-forgot 2-change//
    String pass;
    String conf;
    SharedPreferences preferences;
    private String passSaved;
    private String wordSaved;

    public DialogForgot(Context _context, int mode){
        MODE = mode;
        context=_context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forgotdialog, container, false);
        initial(v);

        if (MODE==Cons.MODE_FORGOT){
            ifForgot();
        }
        if (MODE==Cons.MODE_NEW_PASS){
            ifChangePass();
        }
        if (MODE==Cons.MODE_NEW_WORD){
            ifChangeWord();
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setWindowAnimations(R.anim.abc_slide_in_top);
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean checkPassConfirm(String passi, String confirm){
        Log.d("Forgot", passi + " " + confirm);
        if (passi!=null && passi.length()>=4){
            if (confirm!=null && passi.equals(confirm)){
                Toast.makeText(context, "Passwords good", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        Toast.makeText(context, "Not correct in check", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void savePassword(String passi, String answeri){
        SharedPreferences preferences = context.getSharedPreferences(Cons.APP_PREF, Context.MODE_MULTI_PROCESS);
        if (passi!=null && answeri!=null) {
            preferences.edit().putString(Cons.APP_PREF_PASS, passi).putString(Cons.APP_PREF_WORD, answeri).putBoolean(Cons.APP_PREF_PASS_SET, true).commit();
        }else{
            Toast.makeText(context, "Not correct", Toast.LENGTH_SHORT).show();
        }
    }

    private void ifForgot(){
        anshint.setVisibility(View.VISIBLE);
        answer.setText("");
        answer.setVisibility(View.VISIBLE);
        answer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String input = answer.getText().toString();
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    if (wordSaved.equals(input)){
                        newpas.setVisibility(View.VISIBLE);
                        newpassword.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);
                        confirmpassword.setVisibility(View.VISIBLE);
                    }else{
                        answer.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
        newpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    if (newpassword.getText()!=null && newpassword.getText().length()>=4)
                    confirmpassword.requestFocus();
                    return true;
                }
                return false;
            }
        });
        confirmpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    conf = confirmpassword.getText().toString();
                    pass = newpassword.getText().toString();
                    if (checkPassConfirm(pass, conf)){
                        savePassword(pass, answer.getText().toString());
                        BlockActivity block = (BlockActivity)getActivity();
                        block.renevPassword();
                        dismiss();
                    }else{
                        newpassword.setText("");
                        confirmpassword.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void ifChangePass(){
        oldpassword.setText("");
        old.setVisibility(View.VISIBLE);
        oldpassword.setVisibility(View.VISIBLE);
        oldpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String input = oldpassword.getText().toString();
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    if (passSaved.equals(input)){
                        newpas.setVisibility(View.VISIBLE);
                        newpassword.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);
                        confirmpassword.setVisibility(View.VISIBLE);
                    }else{
                        oldpassword.setText("");
                    }
                    return true;
                }

                return false;
            }
        });
        newpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    if (newpassword.getText()!=null && newpassword.getText().length()>=4)
                        confirmpassword.requestFocus();
                    return true;
                }
                return false;
            }
        });
        confirmpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    conf = confirmpassword.getText().toString();
                    pass = newpassword.getText().toString();
                    if (checkPassConfirm(pass, conf)){
                        savePassword(pass, answer.getText().toString());
                        dismiss();
                    }else{
                        newpassword.setText("");
                        confirmpassword.setText("");
                    }
                    return true;
                }

                return false;
            }
        });
    }

    private void ifChangeWord(){
        anshint.setVisibility(View.VISIBLE);
        answer.setText("");
        answer.setVisibility(View.VISIBLE);
        answer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String input = answer.getText().toString();
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN) &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 || event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                    if (wordSaved.equals(input)) {
                        old.setText("Current password");
                        old.setVisibility(View.VISIBLE);
                        oldpassword.setVisibility(View.VISIBLE);
                    } else {
                        answer.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
        oldpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String oldword = answer.getText().toString();
                String pas = oldpassword.getText().toString();
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    if (wordSaved.equals(oldword) && passSaved.equals(pas)){
                        newpas.setText("New word");
                        newpas.setVisibility(View.VISIBLE);
                        newpassword.setVisibility(View.VISIBLE);
                        newpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        confirm.setText("Confirm word");
                        confirm.setVisibility(View.VISIBLE);
                        confirmpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        confirmpassword.setVisibility(View.VISIBLE);
                    }else{
                        answer.setText("");
                        oldpassword.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
        newpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    if (newpassword.getText()!=null && newpassword.getText().length()>=4)
                        confirmpassword.requestFocus();
                    return true;
                }
                return false;
            }
        });
        confirmpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_GO || keyCode == EditorInfo.IME_ACTION_NEXT || keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN)&&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_L1 ||  event.getKeyCode() == KeyEvent.KEYCODE_SEARCH){
                    conf = confirmpassword.getText().toString();
                    pass = newpassword.getText().toString();
                    if (pass.equals(conf)){
                        preferences = context.getSharedPreferences(Cons.APP_PREF, Context.MODE_MULTI_PROCESS);
                        preferences.edit().remove(Cons.APP_PREF_WORD).putString(Cons.APP_PREF_WORD, pass).commit();
                        dismiss();
                    }else{
                        newpassword.setText("");
                        confirmpassword.setText("");
                    }
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initial(View v){
        preferences = context.getSharedPreferences(Cons.APP_PREF, Context.MODE_MULTI_PROCESS);
        passSaved = preferences.getString(Cons.APP_PREF_PASS, null);
        wordSaved = preferences.getString(Cons.APP_PREF_WORD, null);
        anshint = (TextView)v.findViewById(R.id.anshint);
        answer = (EditText)v.findViewById(R.id.forgotAnswer);
        old = (TextView)v.findViewById(R.id.textViewOld);
        newpas = (TextView)v.findViewById(R.id.textViewNew);
        confirm = (TextView)v.findViewById(R.id.textViewConfirm);
        oldpassword = (EditText)v.findViewById(R.id.forgotOld);
        newpassword = (EditText)v.findViewById(R.id.forgotNew);
        confirmpassword = (EditText)v.findViewById(R.id.forgotConfirm);
        anshint.setVisibility(View.INVISIBLE);
        answer.setVisibility(View.INVISIBLE);
        old.setVisibility(View.INVISIBLE);
        newpas.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        oldpassword.setVisibility(View.INVISIBLE);
        newpassword.setVisibility(View.INVISIBLE);
        confirmpassword.setVisibility(View.INVISIBLE);
    }
}
