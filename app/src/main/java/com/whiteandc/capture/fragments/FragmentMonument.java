package com.whiteandc.capture.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.adapters.SectionsPagerAdapter;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;

import java.lang.reflect.Field;

public class FragmentMonument extends Fragment implements OnPageChangeListener, OnClickListener{


	private static final String CLASS = "FragmentMonument";
	FragmentGallery fragmentGallery = new FragmentGallery();
	FragmentDescription fragmentDescription= new FragmentDescription();
	FragmentCamera fragmentCamera;
	
	private static final Field sChildFragmentManagerField;

    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(CLASS, "Error getting mChildFragmentManager field", e);
        }
        sChildFragmentManagerField = f;
    }
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(CLASS+"->onCreateView", "");

		View rootView = inflater.inflate(R.layout.fragment_monument, container, false);

        loadFragmentToShow();
     
        // Listening capture button
        Button button = (Button) rootView.findViewById(R.id.capture_button);
        button.setOnClickListener(this);
        
		return rootView;
	}

	private boolean isCurrentMonumentCaptured(){
        boolean isCaptured = false;
        MonumentsActivity monumentsActivity = (MonumentsActivity)getActivity();

        if (monumentsActivity != null){
            String currentMonumentId = monumentsActivity.getCurrentMonumentId();
            if(currentMonumentId != null){
                Monument monument =  MonumentList.getMonument(currentMonumentId);
                isCaptured = (monument != null && monument.isCaptured());
            }
        }

		return isCaptured;
	}

	private void loadFragmentToShow() {
		

		FragmentTransaction transaction =  getChildFragmentManager().beginTransaction();
		if(isCurrentMonumentCaptured()){
			Log.i(CLASS, "loadFragmentToShow: fragmentDescription");
			transaction.add(R.id.layoutToReplace, fragmentDescription ).commit();
		}else{
			Log.i(CLASS, "loadFragmentToShow: fragmentGallery");
			transaction.add(R.id.layoutToReplace, fragmentGallery).commit();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int page) {
		Log.i(getClass().getSimpleName(), "onPageSelected: "+page);
		if(isCurrentMonumentCaptured()){
			changeToFragment(fragmentDescription);
		}else{
			changeToFragment(fragmentGallery);
		}
		if (SectionsPagerAdapter.MONUMENT == page){
			if(isCurrentMonumentCaptured()){
				Log.i(getClass().getSimpleName(), "onPageSelected: loading monument data for description");
				fragmentDescription.loadCurrentMonumentData();
			}else{
				Log.i(getClass().getSimpleName(), "onPageSelected: loading monument data for gallery");
				fragmentGallery.loadCurrentMonumentData();
			}

		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.capture_button) {
			fragmentCamera = ((MonumentsActivity) getActivity()).fragmentCamera;
			
			FragmentTransaction transaction =  getChildFragmentManager().beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.layoutToReplace,fragmentCamera).commit();
		}
	}



	public void changeToFragment(Fragment fragment) {
		FragmentTransaction transaction =  getChildFragmentManager().beginTransaction();
		transaction.replace(R.id.layoutToReplace, fragment).commit();
	}

	@Override
	public void onDetach() {
		super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
                Log.e(CLASS, "Error setting mChildFragmentManager field", e);
            }
        }
	}



}
