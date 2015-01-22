package com.clint.test.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by administrator on 1/20/15.
 */
public class Crime {
    private static final String JSON_SUSPECT = "suspect";

    private UUID mId;
    private String mTitle;
    private Date mDAte;
    private boolean mSolved;

    private String mSuspect;

    public Crime() {
        mId = UUID.randomUUID();
        mDAte= new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDAte;
    }

    public void setDAte(Date DAte) {
        mDAte = DAte;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    @Override
    public String toString() {
        return "Crime{" +
                "mTitle='" + mTitle + '\'' +
                '}';
    }
}
