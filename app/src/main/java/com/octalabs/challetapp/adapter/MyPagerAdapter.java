package com.octalabs.challetapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mFragmentsTitle;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentsTitle = new ArrayList<>();
        mFragments = new ArrayList<>();
    }

    public void addFragmeent(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentsTitle.add(title);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mFragments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentsTitle.get(position);
    }

}
