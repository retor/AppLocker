package com.retor.AppLocker;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.*;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Created by Антон on 25.03.14.
 */
public class ListLunchedApps extends ListFragment implements OnItemClickListener{

    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
				getListView().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.listapps, null, false);
        context = getActivity().getApplicationContext();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*        CheckBox cb=(CheckBox)view.findViewById(R.id.checkApp);
        if(!cb.isChecked()) {
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }*/
        ActivityManager.RunningAppProcessInfo ri = (ActivityManager.RunningAppProcessInfo)parent.getItemAtPosition(position);
        int myPid = android.os.Process.myPid();
        int killPid = ri.pid;
        if (killPid!=myPid){
            android.os.Process.sendSignal(killPid, android.os.Process.SIGNAL_KILL);// killProcess(killPid);
this.getListView().invalidate();
            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Can't kill himself", Toast.LENGTH_SHORT).show();
        }


    }
}
