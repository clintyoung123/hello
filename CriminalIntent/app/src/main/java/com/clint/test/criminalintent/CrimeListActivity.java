package com.clint.test.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by administrator on 1/20/15.
 */
public class CrimeListActivity extends SingleFragmentActivity
    implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    //region Interface - CrimeListFramgment.Callbacks
    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detailFragmentContainer) == null){
            // start an instance of CrimePagerActivity
            Intent i = new Intent (this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(i);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetailFragment = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDeatilFragment = CrimeFragment.newInstance(crime.getId());

            if (oldDetailFragment!=null){
                ft.remove(oldDetailFragment);
            }
            ft.add(R.id.detailFragmentContainer, newDeatilFragment);
            ft.commit();
        }
    }
    //endregion

    //region Interface - CrimeFramgment.Callbacks
    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment)fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }
    //endregion
}
