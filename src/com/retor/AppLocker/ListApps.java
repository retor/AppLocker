package com.retor.AppLocker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

/**
 * Created by Антон on 25.03.14.
 */
public class ListApps extends ListFragment implements OnItemClickListener {

    Context context;
    int MAX = 10;


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
        getListView().setSelector(R.drawable.selector);
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
        final InfoFragment dialogFragment = new InfoFragment();
        final FragmentManager fragmentManager = getFragmentManager();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Dialog);
        ListAppsAdapter.ViewHolder vh = (ListAppsAdapter.ViewHolder)view.findViewById(R.layout.app);// parent.getItemAtPosition(position);
        if(!vh.checked){
            //do what you want
            vh.checked=true;
            dialogFragment.show(fragmentManager, "321");
            // view.setSelected(true);
            Toast.makeText(context, "+",Toast.LENGTH_SHORT).show();
        } else {
            vh.checked=false;
            // view.setSelected(false);
            Toast.makeText(context, "-", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, String.valueOf(getListView().getCheckedItemCount()), Toast.LENGTH_SHORT).show();
    }

}
