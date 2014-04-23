package com.retor.AppLocker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Антон on 25.03.14.
 */
public class ListApps extends ListFragment implements OnItemClickListener{

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
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setBackgroundColor(getResources().getColor(R.color.background_default));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.listapps, null, false);
        context = getActivity().getApplicationContext();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!view.isSelected()) {
            view.setBackgroundColor(getResources().getColor(R.color.background_checked));
            view.setSelected(true);
        }else{
            view.setBackgroundColor(getResources().getColor(R.color.background_default));
            view.setSelected(false);
        }

        /*CheckBox cb=(CheckBox)view.findViewById(R.id.checkApp);
        if(!cb.isChecked()) {
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }*/
//        PackageInfo pi = (PackageInfo) parent.getItemAtPosition(position);
        Intent intent = new Intent();
       // intent.putE
        Toast.makeText(context, String.valueOf(getListView().getCheckedItemCount()), Toast.LENGTH_SHORT).show();
    }
}
