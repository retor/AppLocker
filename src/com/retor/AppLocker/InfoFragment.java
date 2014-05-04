package com.retor.AppLocker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by retor on 03.04.2014.
 */
public class InfoFragment extends android.support.v4.app.DialogFragment implements DialogInterface {
    int mNum=0;
    final String TAG = "321";

    public InfoFragment() {
        super();
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
        InfoFragment ii =(InfoFragment) fm.findFragmentByTag(TAG);
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
