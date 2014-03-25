package com.retor.AppLocker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAppsAdapter extends BaseAdapter  {
    Context context;
    ArrayList<ApplicationInfo> appList;
    int res;
    PackageManager pm;

    public ListAppsAdapter(Context _context, ArrayList<ApplicationInfo> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        appList = new ArrayList<ApplicationInfo>();
        appList = _appList;
        pm = _pm;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
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

            String appNameStr = appList.get(position).packageName; //appList.get(position).name + " " + appList.get(position).sourceDir;
            String appOtherStr = appList.get(position).nativeLibraryDir + " " + appList.get(position).taskAffinity;
            Drawable appIco = appList.get(position).loadIcon(pm);

            vh.appName.setText(appNameStr);
            vh.appOther.setText(appOtherStr);
            vh.appIcon.setImageDrawable(appIco);
        }else{
            v = convertView;
        }
        return v;
    }

    public class ViewHolder{
        TextView appName;
        TextView appOther;
        ImageView appIcon;
        CheckBox appCheck;

    }
}
