package com.retor.AppLocker.adapters;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Apps;

import java.util.ArrayList;
import java.util.List;

public class ListAppsAdapter extends BaseAdapter {
    Context context;
    List<PackageInfo> appList;
    int res;
    PackageManager pm;
    ArrayList<Apps> tests;

    public ListAppsAdapter(Context _context, List<PackageInfo> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        appList = new ArrayList<PackageInfo>();
        appList = _appList;
        pm = _pm;
    }

    public ListAppsAdapter(Context _context, ArrayList<Apps> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        tests = new ArrayList<Apps>();
        tests = _appList;
        pm = _pm;
    }

    @Override
    public int getCount() {
        if (appList==null){
            return tests.size();
        }else{
            return appList.size();
        }
    }

    @Override
    public PackageInfo getItem(int position) {
        if (appList==null){
            return tests.get(position);
        }else{
            return appList.get(position);
        }
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(res, null);
        ViewHolder vh = new ViewHolder(v);
        String appNameStr;
        String appOtherStr;
        Drawable appIco;
        if (appList==null){
            appNameStr = tests.get(position).applicationInfo.loadLabel(pm).toString();
            appOtherStr = tests.get(position).applicationInfo.processName.toString();
            appIco = tests.get(position).applicationInfo.loadIcon(pm);
        }else {
            appNameStr = appList.get(position).applicationInfo.loadLabel(pm).toString();
            appOtherStr = appList.get(position).applicationInfo.processName.toString();
            appIco = appList.get(position).applicationInfo.loadIcon(pm);
        }
        vh.appName.setText(appNameStr);
        vh.appOther.setText(appOtherStr);
        vh.appIcon.setImageDrawable(appIco);
        return v;
    }

    public class ViewHolder{
        TextView appName;
        TextView appOther;
        ImageView appIcon;
        public boolean check;
        public ViewHolder(View v){
            appName = (TextView) v.findViewById(R.id.nameApp);
            appOther = (TextView) v.findViewById(R.id.otherApp);
            appIcon = (ImageView) v.findViewById(R.id.iconApp);
            check = false;
        }
    }
}
