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
import android.widget.ImageView;
import android.widget.TextView;
import com.retor.AppLocker.retor4i.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class LunchedAdapter extends BaseAdapter {
    Context context;
    ArrayList<AppInfo> appInfos;
    List<RunningAppProcessInfo> appList;
    int res;
    final PackageManager pm;
    Drawable appIco;

    public LunchedAdapter(PackageManager pm, Context context, ArrayList<AppInfo> appInfos, int res) {
        this.pm = pm;
        this.context = context;
        this.appInfos = new ArrayList<AppInfo>();
        this.appInfos = appInfos;
        this.res = res;
    }

    public LunchedAdapter(Context _context, List<RunningAppProcessInfo> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        appList = new ArrayList<RunningAppProcessInfo>();
        appList = _appList;
        pm = _pm;
    }

    @Override
    public int getCount() {
         return appInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfos.get(position);
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
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(res,null);
        ViewHolder vh = new ViewHolder(v);

        String appNameStr=""; 
        String appOtherStr="";
        Drawable appIcon=null;

        if (appInfos.size()==0) {
             appNameStr = appList.get(position).processName;
             appOtherStr = String.valueOf(appList.get(position).pid);
            try {
                String st = appList.get(position).processName;// .importanceReasonComponent.getPackageName().toString();
                appIcon = pm.getApplicationIcon(pm.getApplicationInfo(st, PackageManager.GET_ACTIVITIES)); //.getApplicationIcon(st) ;//context.getPackageManager().getApplicationLabel(pm.getApplicationInfo(appList.get(position).toString(), PackageManager.GET_META_DATA)));
                Log.i("Icon res", st.toString());
                vh.appIcon.setImageDrawable(appIcon);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }else {
            appNameStr = appInfos.get(position).getAppLabel();//.getPackageName();
            appOtherStr = String.valueOf(appInfos.get(position).getUid());
            appIcon = appInfos.get(position).getIcon();
        }
            vh.appName.setText(appNameStr);
            vh.appOther.setText(appOtherStr);
            //vh.appCheck.setChecked(false);
            vh.appIcon.setImageDrawable(appIcon);
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class ViewHolder{
        TextView appName;
        TextView appOther;
        ImageView appIcon;
    public ViewHolder(View v){
        appName = (TextView) v.findViewById(R.id.nameApp);
        appOther = (TextView) v.findViewById(R.id.otherApp);
        appIcon = (ImageView) v.findViewById(R.id.iconApp);
    }
    }
}
