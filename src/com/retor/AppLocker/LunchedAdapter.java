package com.retor.AppLocker;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
    PackageManager pm;
    Drawable appIco;

    public LunchedAdapter(Context _context, List<RunningAppProcessInfo> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        appList = new ArrayList<RunningAppProcessInfo>();
        appList = _appList;
        pm = _pm;
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
            v = inflater.inflate(res,null);
            vh.appName = (TextView)v.findViewById(R.id.nameApp);
            vh.appOther = (TextView)v.findViewById(R.id.otherApp);
            //vh.appIcon = (ImageView)v.findViewById(R.id.iconApp);
            vh.appCheck = (CheckBox)v.findViewById(R.id.checkApp);

            String appNameStr = appList.get(position).processName;
            String appOtherStr = String.valueOf(appList.get(position).pid);


        try {

            appIco = context.getPackageManager().getApplicationLabel(appList.get(position).toString(), PackageManager.GET_META_DATA).loadIcon(context.getPackageManager());
            // getApplicationIcon(appList.get(position).);
            Log.i("Icon res", String.valueOf(appIco.getMinimumHeight()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.NullPointerException e){
            e.printStackTrace();
        }
        //vh.appIcon.setImageDrawable(appIco);
        //Drawable appIco = appList.get(position).applicationInfo.loadIcon(pm);
            vh.appName.setText(appNameStr);
            vh.appOther.setText(appOtherStr);
            vh.appCheck.setChecked(false);
        return v;
    }

    public class ViewHolder{
        TextView appName;
        TextView appOther;
        ImageView appIcon;
        CheckBox appCheck;
    }
}
