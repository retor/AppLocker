package com.retor.AppLocker.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Apps;

import java.util.List;

/**
 * Created by retor on 03.04.2014.
 */
public class InfoFragment extends android.support.v4.app.DialogFragment implements DialogInterface {
    final String TAG = "321";
    int mNum = 0;
    Apps apps;
    Context context;

    public InfoFragment() {
        super();
    }

    public InfoFragment(Apps apps, Context cont) {
        super();
        context = cont;
        this.apps = apps;
    }

    static InfoFragment newInstance(int num) {
        InfoFragment f = new InfoFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info, container, false);
        TextView name = (TextView) v.findViewById(R.id.textView);
        name.setText(apps.packageName.toString());
        TextView loca = (TextView) v.findViewById(R.id.textView2);
        loca.setText(apps.applicationInfo.sourceDir.toString());
        TextView activity = (TextView) v.findViewById(R.id.infoActivity);
        String act = null;
        final PackageManager pm = context.getPackageManager();
        if (pm != null) {
            try {
                act = pm.getLaunchIntentForPackage(apps.packageName).getComponent().getClassName();
            } catch (NullPointerException e) {
                Log.d("Blya", e.toString());
                Intent intentFilter = new Intent(Intent.ACTION_MAIN, null);
                intentFilter.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> infs = pm.queryIntentActivities(intentFilter, 0);
                for (ResolveInfo infa : infs) {
                    if (infa.toString().contains(apps.packageName)) {//infa.resolvePackageName.contains(tmpApps.packageName))
                        act = infa.resolvePackageName;
                    }
                }
            }
        }
        activity.setText(act);
        return v;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        FragmentManager fm = getFragmentManager();
        InfoFragment ii = (InfoFragment) fm.findFragmentByTag(TAG);
        fm.beginTransaction().remove(ii).commit();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void cancel() {
        final FragmentManager fm = getFragmentManager();
        fm.beginTransaction().remove(fm.findFragmentByTag(TAG)).commit();
    }
}
