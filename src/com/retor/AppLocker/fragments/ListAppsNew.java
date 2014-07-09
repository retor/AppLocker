package com.retor.AppLocker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.retor.AppLocker.R;
import com.retor.AppLocker.adapters.ListAppsAdapter;
import com.retor.AppLocker.classes.AppsToBlock;
import com.retor.AppLocker.classes.Cons;

import java.util.ArrayList;

/**
 * Created by Антон on 25.03.14.
 */
public class ListAppsNew extends ListFragment implements OnItemClickListener {

    Context context;
    static ListAppsAdapter oldAdapter;

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
        final ListAppsAdapter adapt = (ListAppsAdapter) getListAdapter();
        checkSelected(adapt);
        oldAdapter = adapt;
        //getListView().setSelector(R.drawable.selector);
        //android:background="?android:attr/activatedBackgroundIndicator"
/*        //test
        getListView().setItemChecked(2,true);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.listapps, null, false);
        context = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppsToBlock tmpApps = (AppsToBlock) parent.getItemAtPosition(position);
        SharedPreferences preferences = context.getSharedPreferences(Cons.APPS_LOCK, Context.MODE_MULTI_PROCESS);
        if (!tmpApps.isCheck()) {
            tmpApps.setCheck(true);
            createDialog(tmpApps, context);
            vibration(context, 1);
            String act = tmpApps.activityInfo.name;
            if (!preferences.contains(tmpApps.activityInfo.applicationInfo.packageName))
                preferences.edit().putString(tmpApps.activityInfo.applicationInfo.packageName, act).commit();
            Toast.makeText(context, "+", Toast.LENGTH_SHORT).show();
        } else {
            tmpApps.setCheck(false);
            vibration(context, 2);
            preferences.edit().remove(tmpApps.activityInfo.applicationInfo.packageName).commit();
            Toast.makeText(context, "-", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, String.valueOf(getListView().getCheckedItemCount()), Toast.LENGTH_SHORT).show();
    }

    private void vibration(Context _context, int _repeat) {
        Vibrator vibrator = (Vibrator) _context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator())
            switch (_repeat) {
                case 1:
                    vibrator.vibrate(30);
                case 2:
                    vibrator.vibrate(15);
            }
    }

    private void createDialog(AppsToBlock apps, Context cont) {
        InfoFragment dialogFragment = null;
        if (apps != null)
            dialogFragment = new InfoFragment(apps, cont);
        assert dialogFragment != null;
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        dialogFragment.setRetainInstance(true);
        dialogFragment.setCancelable(true);

        final FragmentManager fragmentManager = getFragmentManager();
        dialogFragment.show(fragmentManager, "Apps");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_search){
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searching(s);
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    searching(s);
                    return false;
                }
            });
            searchView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    setListAdapter(oldAdapter);
                    checkSelected(oldAdapter);
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return false;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    searchView.setQuery("", true);
                    return false;
                }
            });
/*            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                }
            });*/
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkSelected(ListAppsAdapter appsAdapter){
        SharedPreferences pref = context.getSharedPreferences(Cons.APPS_LOCK, Context.MODE_MULTI_PROCESS);
        if (pref != null)
            for (int i = 0; i < appsAdapter.getCount(); i++) {
                AppsToBlock checker = (AppsToBlock) appsAdapter.getItem(i);
                if (pref.getString(checker.activityInfo.applicationInfo.packageName, null) != null)
                    checker.setCheck(true);
                if (checker.isCheck())
                    getListView().setItemChecked(i, true);
            }
    }

    private void searching(String toSearch) {
        if (toSearch == null || toSearch.equalsIgnoreCase("") || toSearch.equalsIgnoreCase(" ") || toSearch.length() == 0) {
            {
                setListAdapter(oldAdapter);
                checkSelected(oldAdapter);
            }
        } else {
            ArrayList<AppsToBlock> tmpArray = new ArrayList<AppsToBlock>();
            for (int i = 0; i < oldAdapter.getCount(); i++) {
                AppsToBlock app = (AppsToBlock) oldAdapter.getItem(i);
                String label = app.loadLabel(context.getPackageManager()).toString();
                int labelLeght = label.length();
                int inputLeght = toSearch.length();
                if (labelLeght != 0 && inputLeght != 0) {
                    String tmpL = null;
                    if (inputLeght >= 1 && labelLeght >= inputLeght) {
                        tmpL = label.substring(0, inputLeght);
                        if (tmpL.equalsIgnoreCase(toSearch))
                            tmpArray.add(app);
                    } else if (inputLeght >= 1 && labelLeght < inputLeght) {
                        tmpL = label.substring(0, labelLeght);
                        if (tmpL.equalsIgnoreCase(toSearch.substring(labelLeght)))
                            tmpArray.add(app);
                    } else {
                        setListAdapter(oldAdapter);
                    }
                }
            }
            ListAppsAdapter newAdapter = new ListAppsAdapter(context, tmpArray, R.layout.app, context.getPackageManager());
            setListAdapter(newAdapter);
            getListView().setTextFilterEnabled(true);
            newAdapter.notifyDataSetInvalidated();
            checkSelected(newAdapter);
        }
    }

    static public void setOldAdapter(ListAdapter adapter){
        oldAdapter = (ListAppsAdapter)adapter;
    }
}
