package com.clint.test.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by administrator on 1/20/15.
 */
public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    private Button mSuspectButton;

    //region Required Callback Interface for Hosting Activities
    private Callbacks mCallbacks;   // casted as Activity that hosting/implement this Fragment

    public interface Callbacks {
        void onCrimeUpdated (Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (CrimeFragment.Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //endregion

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // mCrime= new Crime();
        // UUID crimeID = (UUID) getActivity().getIntent().getSerializableExtra(this.EXTRA_CRIME_ID);
        UUID crimeID = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        //UP icon in Actionbar
        if (NavUtils.getParentActivityIntent(getActivity())!=null) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);  //will crash with null ActionBar
            }*/
            ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
            actionBarActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // view items
        mTitleField=(EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                mCallbacks.onCrimeUpdated(mCrime);  // inform hosting Activity to update all its fragment
                getActivity().setTitle(mCrime.getTitle());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                mCallbacks.onCrimeUpdated(mCrime);
            }
        });

        Button reportButton = (Button) v.findViewById(R.id.crime_reportButton);
        reportButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject)+ mCrime.getTitle());
                i.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mSuspectButton = (Button) v.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect()!=null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(mCrime.getTitle());
    }

    // menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                /*// old way
                Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/

                if (NavUtils.getParentActivityIntent(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Send Report
    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (resultCode == this.REQUEST_CONTACT){
            if (data==null) return;

            // get Contact URI to query
            Uri contactUri = data.getData();

            // Fields to Query
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            // do Query from URI
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            if (c==null || c.getCount()==0){
                c.close();
                return;
            }

                // Pull out first collumn of first row
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mCallbacks.onCrimeUpdated(mCrime);
                mSuspectButton.setText(suspect);
                c.close();
        }
    }
}
