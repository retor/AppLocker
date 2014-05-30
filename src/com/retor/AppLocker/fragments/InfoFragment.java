package com.retor.AppLocker.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Apps;

/**
 * Created by retor on 03.04.2014.
 */
public class InfoFragment extends android.support.v4.app.DialogFragment implements DialogInterface {
    final String TAG = "321";
    int mNum = 0;
    Apps apps;

    public InfoFragment() {
        super();
    }

    public InfoFragment(Apps apps) {
        super();
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

        /*View tv = v.findViewById(R.id.text);
        ((TextView)tv).setText("Dialog #" + mNum + ": using style "
                + getNameForNum(mNum));

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.show);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((FragmentDialog)getActivity()).showDialog();
            }
        });*/

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
