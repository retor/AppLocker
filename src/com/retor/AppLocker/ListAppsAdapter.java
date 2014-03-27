package com.retor.AppLocker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class ListAppsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
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
        ViewHolder vh = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            v = inflater.inflate(res,null);
            vh.appName = (TextView)v.findViewById(R.id.nameApp);
            vh.appOther = (TextView)v.findViewById(R.id.otherApp);
            vh.appIcon = (ImageView)v.findViewById(R.id.iconApp);
            vh.appCheck = (CheckBox)v.findViewById(R.id.checkApp);


            String appNameStr = appList.get(position).applicationInfo.loadLabel(pm).toString();//appList.get(position).name + " " + appList.get(position).sourceDir;
            String appOtherStr = appList.get(position).applicationInfo.nativeLibraryDir + " " + pm.getLaunchIntentForPackage(appList.get(position).packageName);
            Drawable appIco = appList.get(position).applicationInfo.loadIcon(pm);

            vh.appName.setText(appNameStr);
            vh.appOther.setText(appOtherStr);
            vh.appCheck.setChecked(false);
            vh.appIcon.setImageDrawable(appIco);
        }else{
            v = convertView;
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       /* CheckBox checkBox;
        view.setSelected(true);
        checkBox = (CheckBox)view.findViewById(R.id.checkApp);
        checkBox.setChecked(true);*/

    }

    public class ViewHolder{
        TextView appName;
        TextView appOther;
        ImageView appIcon;
        CheckBox appCheck;

    }
}
