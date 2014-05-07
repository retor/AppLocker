package com.retor.AppLocker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
        getListView().setSelector(R.drawable.selector);
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
    public void onListItemClick(ListView lv, View view, int position, long id) {

        if (!lv.getChildAt(position).isSelected()) {
            lv.getChildAt(position).setSelected(true);
            Toast.makeText(context,"+",Toast.LENGTH_SHORT).show();
        }else{
            lv.getChildAt(position).setSelected(false);
            Toast.makeText(context,"-",Toast.LENGTH_SHORT).show();
        }


        super.onListItemClick(lv, view, position, id);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lv = (ListView)parent;
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        for (int i=0; i<checked.size();i++){
            if (checked.get(i) == true){
                Toast.makeText(context, String.valueOf(checked.get(i)),Toast.LENGTH_SHORT).show();
            }
        }


/*        final InfoFragment dialogFragment = new InfoFragment();
        final FragmentManager fragmentManager = getFragmentManager();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Dialog);
//        Activity am = (Activity)context.getSystemService(Context.ACTIVITY_SERVICE);
//        ActionBar ab = (ActionBar)am.getActionBar();
        Vibrator vib;
        vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(100);*/
//        String actionString = String.valueOf(position);
//        ab.setTitle(actionString);
        //View vv = (View)parent.getItemAtPosition(position);

        //ListAppsAdapter.ViewHolder vh = (ListAppsAdapter.ViewHolder)view.findViewById(R.layout.app);// parent.getItemAtPosition(position);

/*            if(!vv.isSelected()){
                //do what you want
                vv.setSelected(true);
                dialogFragment.show(fragmentManager, "321");
                // view.setSelected(true);
                Toast.makeText(context, "+",Toast.LENGTH_SHORT).show();
            } else {
                vv.setSelected(false);
                // view.setSelected(false);
                Toast.makeText(context, "-", Toast.LENGTH_SHORT).show();
            }*/

        Toast.makeText(context, String.valueOf(getListView().getCheckedItemCount()), Toast.LENGTH_SHORT).show();
    }

}
