package com.shasratech.s_c_31;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPageTabAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public MainPageTabAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
          /*  case 0: return new DailyFragment();
            case 1: return new MonthlyFragment();
            case 2: return new YearlyFragment();
            case 3: return new CompleteFragment(); */

        }
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
