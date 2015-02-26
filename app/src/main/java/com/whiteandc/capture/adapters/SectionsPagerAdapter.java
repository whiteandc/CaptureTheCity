package com.whiteandc.capture.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.fragments.FragmentCityList;
import com.whiteandc.capture.fragments.FragmentMonument;

import java.util.Locale;


/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String CLASS = "SectionsPagerAdapter";
	public static final int CITY_LIST = 0;
	public static final int MONUMENT = 1;
	private MonumentsActivity activity = null;
	public FragmentCityList fragmentCityList= new FragmentCityList();
	public FragmentMonument fragmentMonument= new FragmentMonument();
		
	public SectionsPagerAdapter(FragmentManager fm, MonumentsActivity context) {
		super(fm);
		this.activity = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;

		if(position== CITY_LIST){
			fragment = fragmentCityList;
		}else if(position== MONUMENT){
            fragment= fragmentMonument;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return activity.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return activity.getString(R.string.title_section2).toUpperCase(l);
		}
		return null;
	}
}