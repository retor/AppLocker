package com.retor.AppLocker.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Apps;
import com.retor.AppLocker.classes.AppsToBlock;
import com.retor.AppLocker.classes.Cons;

import java.util.ArrayList;
import java.util.List;

public class ListAppsAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<AppsToBlock> appList;
    int res;
    PackageManager pm;
    ArrayList<Apps> tests;
    MyFilterListApps myfilter;

    public ListAppsAdapter(Context _context, List<AppsToBlock> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        appList = new ArrayList<AppsToBlock>(_appList);
        //appList = _appList;
        pm = _pm;
    }

    public ListAppsAdapter(Context _context, ArrayList<Apps> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        tests = new ArrayList<Apps>();
        tests = _appList;
        pm = _pm;
        appList = null;
    }

    @Override
    public int getCount() {
        if (appList == null) {
            return tests.size();
        } else {
            return appList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (appList == null) {
            return tests.get(position);
        } else {
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
        checkSelected(appList.get(position));
        ViewHolder vh = new ViewHolder(v);
        String appNameStr;
        String appOtherStr;
        Drawable appIco;
        Drawable lock =null;
        if (appList == null) {
            appNameStr = tests.get(position).applicationInfo.loadLabel(pm).toString();
            appOtherStr = tests.get(position).applicationInfo.processName.toString();
            appIco = tests.get(position).applicationInfo.loadIcon(pm);
        } else {
            if (appList.get(position).isPack()) {
                appNameStr = appList.get(position).getPackApps();
            } else {
                appNameStr = appList.get(position).activityInfo.loadLabel(pm).toString();
            }
            appOtherStr = appList.get(position).activityInfo.processName.toString();
            appIco = appList.get(position).loadIcon(pm);
            if (appList.get(position).isCheck()){
                lock = context.getResources().getDrawable(R.drawable.lock_ic);
            }else{
                lock = context.getResources().getDrawable(R.drawable.unlock_ic);
            }
        }
        vh.appName.setText(appNameStr);
        vh.appOther.setText(appOtherStr);
        vh.appIcon.setImageDrawable(appIco);
        vh.lock.setImageDrawable(lock);
        return v;
    }

    @Override
    public Filter getFilter() {
        if (myfilter == null)
            myfilter = new MyFilterListApps();
        return myfilter;
    }

    public class ViewHolder {
        public boolean check;
        TextView appName;
        TextView appOther;
        ImageView appIcon;
        ImageView lock;

        public ViewHolder(View v) {
            appName = (TextView) v.findViewById(R.id.nameApp);
            appOther = (TextView) v.findViewById(R.id.otherApp);
            appIcon = (ImageView) v.findViewById(R.id.iconApp);
            lock = (ImageView) v.findViewById(R.id.imageLock);
            check = false;
        }
    }

    private class MyFilterListApps extends Filter {
        final ArrayList<AppsToBlock> filter;

        public MyFilterListApps() {
            filter = new ArrayList<AppsToBlock>(appList);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == "" || constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = filter;
                results.count = filter.size();
            } else {
                // We perform filtering operation
                ArrayList<AppsToBlock> tmpApps = new ArrayList<AppsToBlock>();
                for (AppsToBlock app : filter) {
                    if (app.loadLabel(pm).toString().toLowerCase().startsWith(constraint.toString().toLowerCase()))
                        tmpApps.add(app);
                }
                results.values = tmpApps;
                results.count = tmpApps.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0 || constraint == null) {
                appList = filter;
                notifyDataSetChanged();
                //notifyDataSetInvalidated();
            }
            if (results.count>0 || constraint!=null){
                appList = (ArrayList<AppsToBlock>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    private void checkSelected(AppsToBlock app) {
        SharedPreferences pref = context.getSharedPreferences(Cons.APPS_LOCK, Context.MODE_MULTI_PROCESS);
        if (pref != null && app!=null){
            String tmpName = app.activityInfo.applicationInfo.packageName;
               if (pref.getString(tmpName, null) != null) {
                app.setCheck(true);
            }
        }
    }
}
