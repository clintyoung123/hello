package com.clint.test.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by administrator on 1/21/15.
 */
public class CrimePagerActivity extends ActionBarActivity/*FragmentActivity*/
    implements CrimeFragment.Callbacks{

    private ViewPager mViewPager;

    private ArrayList<Crime> mCrimes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // view Pager (frame view)
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        // adapter
        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        // set page change listener
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                Crime crime = mCrimes.get(i);
                if (crime.getTitle()!= null){
                    setTitle(crime.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        // initial page
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i=0; i<mCrimes.size(); i++){
            if (mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    //region Interface - CrimeFramgment.Callbacks
    @Override
    public void onCrimeUpdated(Crime crime) {
    }
    //endregion
}
