package com.retor.AppLocker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAppsAdapter extends BaseAdapter {
    Context context;
    List<PackageInfo> appList;
    int res;
    PackageManager pm;

    public ListAppsAdapter(Context _context, List<PackageInfo> _appList, int _res, PackageManager _pm) {
        context = _context;
        res = _res;
        appList = new ArrayList<PackageInfo>();
        appList = _appList;
        pm = _pm;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public PackageInfo getItem(int position) {
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
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(res,null);
        ViewHolder vh = new ViewHolder(v);

            String appNameStr = appList.get(position).applicationInfo.loadLabel(pm).toString();
            String appOtherStr = appList.get(position).applicationInfo.processName.toString();
            Drawable appIco = appList.get(position).applicationInfo.loadIcon(pm);

        vh.appName.setText(appNameStr);
        vh.appOther.setText(appOtherStr);
        vh.appIcon.setImageDrawable(appIco);
        return v;
    }

    public class ViewHolder implements Checkable{
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

        /**
         * Change the checked state of the view
         *
         * @param checked The new checked state
         */
        @Override
        public void setChecked(boolean checked) {
            check=checked;
        }

        /**
         * @return The current checked state of the view
         */
        @Override
        public boolean isChecked() {
            return check;
        }

        /**
         * Change the checked state of the view to the inverse of its current state
         */
        @Override
        public void toggle() {
            if (isChecked())check=false;
        }
    }
}
