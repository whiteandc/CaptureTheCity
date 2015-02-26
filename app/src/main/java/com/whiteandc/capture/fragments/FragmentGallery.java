package com.whiteandc.capture.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.adapters.ViewPagerAdapter;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;

import java.lang.reflect.Field;

public class FragmentGallery extends Fragment {
	private static final String CLASS = "FragmentGallery";
	
	
	ViewPager viewPager = null;
	ViewPagerAdapter adapter;
	View rootView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(CLASS+"->onCreateView", "Creando vista fragment gallery");

		rootView = inflater.inflate(R.layout.fragment_monument_not_captured, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.photo_pager);

		loadCurrentMonumentData();
		
        //Bind the title indicator to the adapter
        CirclePageIndicator circleIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setOnPageChangeListener(((MonumentsActivity) getActivity()));

        rootView.findViewById(R.id.pink_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();
            }
        });

		return rootView;
	}

	public void loadCurrentMonumentData() {
		if (rootView != null){
			if(((MonumentsActivity) getActivity())==null){
				Log.e(CLASS, "Activity is null");
			}
			((MonumentsActivity) getParentFragment().getActivity()).onPageSelected(0);

			Monument monument= MonumentList.getMonument(((MonumentsActivity) getParentFragment().getActivity()).getCurrentMonumentId());

			//TextView textView = (TextView) rootView.findViewById(R.id.monument_name);
			//textView.setText(monument.getName());

			adapter= new ViewPagerAdapter(getActivity(), monument.getPhotos());
			 // Pass results to ViewPagerAdapter Class
			adapter.setMonument(monument.getPhotos());
			adapter.notifyDataSetChanged();
	        // Binds the Adapter to the ViewPager
	        viewPager.setAdapter(adapter);
		}
	}

	@Override
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}

}
