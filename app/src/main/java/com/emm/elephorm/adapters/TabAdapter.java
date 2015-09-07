package com.emm.elephorm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.emm.elephorm.fragments.TabCategoriesFragment;
import com.emm.elephorm.fragments.TabHistoryFragment;
import com.emm.elephorm.fragments.TabHomeFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TabHomeFragment();
            case 1:
                return new TabCategoriesFragment();
            case 2:
                return new TabHistoryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}