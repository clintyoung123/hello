package com.clint.test.criminalintent;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by administrator on 1/20/15.
 */
public class CrimeListFragment extends ListFragment {

    private static final String TAG = "CrimeListFragment";
    private boolean mSubtitleVisible;

    private ArrayList<Crime> mCrimes;

    //region Required Callback Interface for Hosting Activities
    private Callbacks mCallbacks;   // casted as Activity that hosting/implement this Fragment

    public interface Callbacks {
        void onCrimeSelected (Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (CrimeListFragment.Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);


        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);

        setListAdapter(adapter);

        //instance saved
        setRetainInstance(true);
        mSubtitleVisible=false;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if (mSubtitleVisible){
            ActionBarActivity actionbarActivity = (ActionBarActivity) getActivity();
            actionbarActivity.getSupportActionBar().setSubtitle(R.string.subtitle);
        }
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked!");

        //inform hosting List Activity specific crime clicked, and start CrimePagerActivity for it
        mCallbacks.onCrimeSelected(c);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {

        private CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), 0 , crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // reuse item view if available
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, parent, false);
            }

            // configure the view with Crime
            Crime c = getItem(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            TextView dateTextView =
                    (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());
            CheckBox solvedCheckBox =
                    (CheckBox)convertView.findViewById(R.id.crime_list_item_resolvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());
            return convertView;

        }
    }

    // menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (showSubtitle!=null && mSubtitleVisible){
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);

                updateUI();
                mCallbacks.onCrimeSelected(crime);

                return true;
            case R.id.menu_item_show_subtitle:
                ActionBarActivity actionBarActivity = ((ActionBarActivity)getActivity());
                if (actionBarActivity.getSupportActionBar().getSubtitle()==null){
                    actionBarActivity.getSupportActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible=true;
                    item.setTitle(R.string.hide_subtitle);
                }else {
                    actionBarActivity.getSupportActionBar().setSubtitle(null);
                    mSubtitleVisible=false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

}
