package com.retor.AppLocker.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.AppsToBlock;

/**
 * Created by retor on 03.04.2014.
 */
public class InfoFragment extends android.support.v4.app.DialogFragment implements DialogInterface {
    final String TAG = "321";
    int mNum = 0;
    AppsToBlock apps;
    Context context;
    PackageManager pm;

    public InfoFragment() {
        super();
    }

    public InfoFragment(AppsToBlock apps, Context cont) {
        super();
        context = cont;
        this.apps = apps;
        pm = cont.getPackageManager();
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
        name.setText(apps.loadLabel(context.getPackageManager()).toString());
        TextView loca = (TextView) v.findViewById(R.id.textView2);
        loca.setText(apps.activityInfo.applicationInfo.sourceDir.toString());
        TextView activity = (TextView) v.findViewById(R.id.infoActivity);
        activity.setText((apps.activityInfo.packageName));
        ImageView ico = (ImageView)v.findViewById(R.id.infoIco);
        ico.setImageDrawable(apps.activityInfo.loadIcon(pm));
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
