package com.retor.AppLocker;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LunchedAdapter extends BaseAdapter {
    Context context;
    List<RunningAppProcessInfo> appList;
    int res;

    public LunchedAdapter(Context _context, List<RunningAppProcessInfo> _appList, int _res) {
        context = _context;
        res = _res;
        appList = new ArrayList<RunningAppProcessInfo>();
        appList = _appList;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public RunningAppProcessInfo getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        ViewHolder vh = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            v = inflater.inflate(res,null);
            vh.appName = (TextView)v.findViewById(R.id.nameApp);
            vh.appOther = (TextView)v.findViewById(R.id.otherApp);
            vh.appIcon = (ImageView)v.findViewById(R.id.iconApp);
            vh.appCheck = (CheckBox)v.findViewById(R.id.checkApp);

            String appNameStr = appList.get(position).processName.toString();
            String appOtherStr = appList.get(position).importanceReasonComponent.toString();// .applicationInfo.processName.toString();//.applicationInfo.manageSpaceActivityName;//.metaData.getClassLoader().toString();//pm.getLaunchIntentForPackage(appList.get(position).applicationInfo.packageName);
            //Drawable appIco = appList.get(position).applicationInfo.loadIcon(pm);

            vh.appName.setText(appNameStr);
            vh.appOther.setText(appOtherStr);
            vh.appCheck.setChecked(false);
           // vh.appIcon.setImageDrawable(appIco);
        }else{
            v = convertView;
        }
        return v;
    }

/*    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox checkBox;
        view.setSelected(true);
        checkBox = (CheckBox)view.findViewById(R.id.checkApp);
        checkBox.setChecked(true);

    }*/

    public class ViewHolder{
        TextView appName;
        TextView appOther;
        ImageView appIcon;
        CheckBox appCheck;

    }
}
