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
import com.retor.AppLocker.retor4i.AppInfo;

/**
 * Created by Антон on 25.03.14.
 */
public class ListLunchedApps extends ListFragment implements OnItemClickListener{

    private Context context;

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
/*        int countList = getListView().getCount();
        for (int i = 0; i<countList;i++){
            AppInfo tmp = (AppInfo)getListView().getItemAtPosition(i);
            if (tmp.isChecked()) {
                getListView().getChildAt(i).setSelected(true);
            }
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.listapps, null, false);
        context = getActivity().getApplicationContext();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        AppInfo appInfo=(AppInfo)parent.getItemAtPosition(position);
        this.appKilling(appInfo,activityManager);
    }

    private void appKilling(AppInfo appInfo, ActivityManager activityManager){
        int appUid = android.os.Process.myUid();
        int killUid = appInfo.getUid();
        if (killUid!=appUid){
            android.os.Process.sendSignal(appInfo.getPid(), 9);
            android.os.Process.killProcess(appInfo.getPid());
            activityManager.killBackgroundProcesses(appInfo.getPackageName());
            Toast.makeText(context, "App: "+ appInfo.getAppLabel()+" killed.", Toast.LENGTH_SHORT).show();
            LunchedAdapter lunchedAdapter = (LunchedAdapter)getListAdapter();
            lunchedAdapter.appInfos = appInfo.getListAppInfo(activityManager.getRunningAppProcesses(), context);
            lunchedAdapter.notifyDataSetInvalidated();
            lunchedAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(context, "Can't kill himself", Toast.LENGTH_SHORT).show();
        }
    }
}
