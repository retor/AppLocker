package com.retor.AppLocker.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.retor.AppLocker.R;
import com.retor.AppLocker.classes.Apps;

/**
 * Created by Антон on 25.03.14.
 */
public class ListApps extends ListFragment implements OnItemClickListener {

    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		getListView().setOnItemClickListener(this);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        //getListView().setSelector(R.drawable.selector);
        //android:background="?android:attr/activatedBackgroundIndicator"
/*        //test
        getListView().setItemChecked(2,true);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.listapps, null, false);
        context = getActivity().getApplicationContext();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Apps tmpApps = (Apps) parent.getItemAtPosition(position);
        assert tmpApps != null;
        if (!tmpApps.isCheck()){
            tmpApps.setCheck(true);
            createDialog(tmpApps);
            vibration(context, 1);
            Toast.makeText(context, "+",Toast.LENGTH_SHORT).show();
        }else{
            tmpApps.setCheck(false);
            vibration(context, 2);
            Toast.makeText(context, "-",Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, String.valueOf(getListView().getCheckedItemCount()), Toast.LENGTH_SHORT).show();
    }

    private void vibration(Context _context, int _repeat){
        Vibrator vibrator = (Vibrator)_context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator())
        switch (_repeat) {
            case 1:
                vibrator.vibrate(30);
            case 2:
                vibrator.vibrate(15);
        }
    }

    private void createDialog(Apps apps){
        InfoFragment dialogFragment = null;
        if (apps!=null)
        dialogFragment = new InfoFragment(apps);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Dialog);
        final FragmentManager fragmentManager = getFragmentManager();
        dialogFragment.show(fragmentManager, "Apps");
    }

}
