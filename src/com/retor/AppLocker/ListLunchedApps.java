package com.retor.AppLocker;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.retor.AppLocker.retor4i.AppInfo;

import java.util.ArrayList;

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
        ActivityManager.RunningAppProcessInfo ri = (ActivityManager.RunningAppProcessInfo)parent.getItemAtPosition(position);
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(((ActivityManager.RunningAppProcessInfo) parent.getItemAtPosition(position)).processName);

        int myPid = android.os.Process.myPid();
        int killPid = ri.pid;
        if (killPid!=myPid){
           /* Process.sendSignal(killPid, Process.SIGNAL_KILL);
            Process.killProcess(killPid);*/
            LunchedAdapter la = (LunchedAdapter)getListAdapter();//notifyDataSetChanged();
            AppInfo appo = new AppInfo();
            ArrayList<AppInfo> app = appo.getListAppInfo(am.getRunningAppProcesses());
            la.appInfos = app;
            //la.appList = am.getRunningAppProcesses();
            la.notifyDataSetInvalidated();
            la.notifyDataSetChanged();
            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Can't kill himself", Toast.LENGTH_SHORT).show();
        }
    }
}
