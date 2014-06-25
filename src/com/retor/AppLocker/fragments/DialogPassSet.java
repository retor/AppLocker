package com.retor.AppLocker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Cons;

/**
 * Created by Антон on 24.06.2014.
 */
public class DialogPassSet extends DialogFragment {
    Context context;

    public DialogPassSet(Context _context){
        context=_context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.passsetter, container, false);
        Button save = (Button)v.findViewById(R.id.setPassButton);
        final EditText pass = (EditText)v.findViewById(R.id.password);
        final EditText confirm = (EditText)v.findViewById(R.id.confirmation);
        final EditText answer = (EditText)v.findViewById(R.id.answer);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPassConfirm(pass.getText().toString(), confirm.getText().toString())) {
                    savePassword(pass.getText().toString(), answer.getText().toString());
                    dismiss();
                }
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean checkPassConfirm(String pass, String confirm){
        if (pass!=null && pass.length()>=4){
            if (confirm!=null && confirm.equals(pass)){
                Toast.makeText(context, "Passwords good", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        Toast.makeText(context, "Not correct in check", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void savePassword(String pass, String answer){
        SharedPreferences preferences = context.getSharedPreferences(Cons.APP_PREF, Context.MODE_MULTI_PROCESS);
        if (pass!=null && answer!=null) {
            preferences.edit().putString(Cons.APP_PREF_PASS, pass).putString(Cons.APP_PREF_WORD, answer).putBoolean(Cons.APP_PREF_PASS_SET, true).commit();
        }else{
            Toast.makeText(context, "Not correct", Toast.LENGTH_SHORT).show();
        }
    }
}
