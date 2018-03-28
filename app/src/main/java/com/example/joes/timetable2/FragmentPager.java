package com.example.joes.timetable2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Joes on 27/3/2018.
 */

public class FragmentPager extends FragmentPagerAdapter{
    ArrayList<Fragment> Pages = new ArrayList<>();

    public FragmentPager(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TimeTableFragment timeTableFragment = TimeTableFragment.newInstance(position);

        return timeTableFragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    //Set title for tabs

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MON";
            case 1:
                return "TUE";
            case 2:
                return "WED";
            case 3:
                return "THU";
            case 4:
                return "FRI";
        }
        return null;
    }
}

