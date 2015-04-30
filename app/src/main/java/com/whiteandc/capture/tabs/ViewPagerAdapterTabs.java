package com.whiteandc.capture.tabs;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by Blanca on 25/04/2015.
 */
public class ViewPagerAdapterTabs extends FragmentPagerAdapter {

    private CharSequence titles[]; // This will Store the titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int numbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private Fragment fragmentLeft;
    private Fragment fragmentRight;
    private FragmentManager fragmentManager;
    private boolean isAdapterList= true;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterTabs(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Fragment fragL, Fragment fragR) {
        super(fm);
        fragmentManager= fm;
        this.titles = mTitles;
        this.numbOfTabs = mNumbOfTabsumb;
        this.fragmentLeft= fragL;
        this.fragmentRight= fragR;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return fragmentLeft;
        } else {
            return fragmentRight;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return numbOfTabs;
    }


    public void deleteFragments() {
        fragmentManager.beginTransaction().remove(fragmentLeft).commit();
        fragmentManager.beginTransaction().remove(fragmentRight).commit();
    }

    public void setFragments(Fragment fragmentL, Fragment fragmentR) {
        fragmentLeft= fragmentL;
        fragmentRight= fragmentR;
    }

    public boolean isAdapterList() {
        return isAdapterList;
    }

    public void setAdapterList(boolean isAdapterList) {
        this.isAdapterList = isAdapterList;
    }

    public void setTitles(CharSequence[] titles) {
        this.titles = titles;
    }
}